package com.recognitron.fruitrecognizer.client;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new GetRecognitionResponseTask(this).execute();
    }

    public void reportWrongRecognition(View view) {}

    public void showFruitInfo(View view) {}

    private static class GetRecognitionResponseTask extends AsyncTask<Void, Void, RequestSender.PostResponse> {

        private WeakReference<ResultActivity> activityRef;

        GetRecognitionResponseTask(ResultActivity context) {
            activityRef = new WeakReference<>(context);
        }

        @Override
        protected RequestSender.PostResponse doInBackground(Void... voids) {
            return new RequestSender(ServerConfig.SERVER_URI)
                    .postWithByteData(
                            ServerConfig.REQUEST_RECOGNITION_PATH,
                            BitmapHolder.getInstance().getByteArray(),
                            "image",
                            "image",
                            "image/*png"
                    );
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
            ImageView imageView = activity.findViewById(R.id.imageView);
            Bitmap blurredBitmap = BitmapHolder.getInstance().getBlurredBitmap();
            if(blurredBitmap != null && response.isReadable())
                imageView.setImageBitmap(blurredBitmap);

            TextView textView = activity.findViewById(R.id.textResult);
            textView.setText(text);
        }
    }
}
