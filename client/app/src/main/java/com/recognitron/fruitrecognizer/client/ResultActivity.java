package com.recognitron.fruitrecognizer.client;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
    }

    @Override
    protected void onStart() {
        super.onStart();

        RequestSender.PostResponse response = new RequestSender(ServerConfig.SERVER_URI)
                        .postWithByteData(
                                ServerConfig.REQUEST_RECOGNITION_PATH,
                                BitmapHolder.getInstance().getByteArray(),
                                "image",
                                "image",
                                "image/*png"
                        );

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

        TextView textView = findViewById(R.id.textResult);
        textView.setText(text);

        ImageView imageView = findViewById(R.id.imageView);
        Bitmap blurredBitmap = BitmapHolder.getInstance().getBlurredBitmap();
        if(blurredBitmap != null && response.isReadable())
            imageView.setImageBitmap(blurredBitmap);
    }

    public void reportWrongRecognition(View view) {}

    public void showFruitInfo(View view) {}

}
