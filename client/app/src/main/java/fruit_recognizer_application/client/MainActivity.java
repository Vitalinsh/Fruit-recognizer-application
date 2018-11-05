package fruit_recognizer_application.client;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "www.com";
    static final String RESULT_KEY = "RESULT";
    private static final int REQUEST_IMAGE_CAPTURE = 0;
    private static final int REQUEST_IMAGE_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void getCameraImage(View view) {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
    }

    public void getGalleryImage(View view) {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , REQUEST_IMAGE_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if ((requestCode == REQUEST_IMAGE_CAPTURE || requestCode == REQUEST_IMAGE_PICK) && resultCode == RESULT_OK) {
            Uri selectedImage = imageReturnedIntent.getData();
            Bitmap fruitImage;
            try {
                fruitImage = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            } catch (FileNotFoundException e) {
                Toast notFoundToast = Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT);
                notFoundToast.show();
                return;
            } catch (IOException e) {
                Toast IOToast = Toast.makeText(this, "Unable to read image", Toast.LENGTH_SHORT);
                IOToast.show();
                return;
            }
            String result = new RequestSender(BASE_URL).requestRecognition("/api/recognize", fruitImage);
            Intent showResult = new Intent(this, ResultActivity.class);
            showResult.putExtra(RESULT_KEY, result);
            startActivity(showResult);
        }
    }
}
