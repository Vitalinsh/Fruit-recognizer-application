package com.recognitron.fruitrecognizer.client;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Random;

public class ResultActivity extends AppCompatActivity {

    private static final String TAG = "ResultActivity";
    private static AsyncTask<Void, Void, RequestSender.PostResponse> requestTask;
    private static final int DELAY_CAP = 120;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        requestTask = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "Requesting recognition...");
        requestTask = new RequestTask(this).execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (requestTask != null) {
            if (requestTask.cancel(true))
                Log.i(TAG, "Request cancelled");

        }
    }

    public void reportWrongRecognition(View view) {}

    public void showFruitInfo(View view) {}

    private static class RequestTask extends AsyncTask<Void, Void, RequestSender.PostResponse> {

        private WeakReference<ResultActivity> activityRef;

        RequestTask(ResultActivity context) {
            activityRef = new WeakReference<>(context);
        }

        @Override
        protected RequestSender.PostResponse doInBackground(Void... voids) {
            RequestSender sender = new RequestSender(ServerConfig.SERVER_URI);
            RequestSender.PostResponse response = null;
            int secDelay = 5;
            int delayDelta = 2;
            do {
                Random rand = new Random(System.currentTimeMillis());
                try {
                    response = sender
                            .postWithByteData(
                                    ServerConfig.REQUEST_RECOGNITION_PATH,
                                    BitmapHolder.getInstance().getByteArray(),
                                    "image",
                                    "image",
                                    "image/*png"
                            );
                } catch (IOException e) {
                    try {
                        long delay = secDelay + rand.nextInt(delayDelta);
                        Log.i(TAG, "Unable to connect to server. Reconnecting in " + delay + " seconds");
                        Thread.sleep(delay * 1000);
                    } catch (InterruptedException e1) {
                        continue;
                    }
                    if (secDelay < DELAY_CAP) {
                        secDelay = secDelay * 2;
                        delayDelta = delayDelta * 2;
                    }
                    continue;
                }
                Log.i(TAG, "Successfully connected to server");
                break;
            } while (!isCancelled());

            return response;
        }

        @Override
        protected void onPostExecute(RequestSender.PostResponse response) {
            super.onPostExecute(response);

            ResultActivity activity = activityRef.get();
            String text;
            if (!response.isReadable()) {
                text = response.getMessage();
            } else {
                try {
                    text = new JSONObject(response.getMessage()).getString("message");
                } catch (JSONException e) {
                    text = response.getMessage();
                }
            }
            Log.i(TAG, "Recieved message: " + text);
            ImageView imageView = activity.findViewById(R.id.imageView);
            Bitmap blurredBitmap = BitmapHolder.getInstance().getBlurredBitmap();
            if(blurredBitmap != null && response.isReadable())
                imageView.setImageBitmap(blurredBitmap);

            TextView textView = activity.findViewById(R.id.textResult);
            textView.setText(text);
        }
    }
}
