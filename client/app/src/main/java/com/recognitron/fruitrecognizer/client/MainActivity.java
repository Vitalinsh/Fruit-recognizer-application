package com.recognitron.fruitrecognizer.client;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 0;
    private static final int REQUEST_IMAGE_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void getCameraImage(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_IMAGE_CAPTURE);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    public void getGalleryImage(View view) {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (pickPhotoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK);
        }
    }

    public void showHistory(View view) {}

    public void showAppInfo(View view) {
        String info = "Fruit Recognizer\nVersion: " + BuildConfig.VERSION_NAME + "\nby RecognitronTeam";
        Toast IOToast = Toast.makeText(this, info, Toast.LENGTH_SHORT);
        IOToast.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (resultCode == RESULT_OK) {
            Bitmap fruitBitmap = null;

            if (requestCode == REQUEST_IMAGE_PICK) {
                Uri selectedImageURI = imageReturnedIntent.getData();

                try {
                    fruitBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageURI);
                } catch (FileNotFoundException e) {
                    Toast notFoundToast = Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT);
                    notFoundToast.show();
                    return;
                } catch (IOException e) {
                    Toast IOToast = Toast.makeText(this, "Unable to read image", Toast.LENGTH_SHORT);
                    IOToast.show();
                    return;
                }
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                try {
                    fruitBitmap = (Bitmap)imageReturnedIntent.getExtras().get("data");
                } catch (NullPointerException e) {
                    Toast badImageData = Toast.makeText(this, "Bad image data", Toast.LENGTH_SHORT);
                    badImageData.show();
                    return;
                }
            }
            BitmapHolder.getInstance().setBitmap(fruitBitmap);

            Intent showResultIntent = new Intent(this, ResultActivity.class);
            startActivity(showResultIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] res) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (res.length > 0 && res[0] == PackageManager.PERMISSION_GRANTED) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }
    }
}


