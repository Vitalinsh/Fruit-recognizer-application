package com.recognitron.fruitrecognizer.client;

import android.graphics.*;
import java.io.ByteArrayOutputStream;

class BitmapHolder {

    private Bitmap bitmap;

    Bitmap getBitmap() {
        return bitmap;
    }

    void setBitmap(Bitmap bitmap) {
        if(bitmap == null) {
            this.bitmap = null;
            return;
        }
        Bitmap squareBitmap;

        if (bitmap.getWidth() >= bitmap.getHeight()){
            squareBitmap = Bitmap.createBitmap(
                    bitmap,
                    bitmap.getWidth()/2 - bitmap.getHeight()/2,
                    0,
                    bitmap.getHeight(),
                    bitmap.getHeight()
            );
        } else {
            squareBitmap = Bitmap.createBitmap(
                    bitmap,
                    0,
                    bitmap.getHeight()/2 - bitmap.getWidth()/2,
                    bitmap.getWidth(),
                    bitmap.getWidth()
            );
        }
        if (squareBitmap.getHeight() > 1024 || squareBitmap.getWidth() > 1024) {
            squareBitmap = Bitmap.createScaledBitmap(squareBitmap, 1024, 1024, false);
        }
        this.bitmap = squareBitmap;
    }

    private static final BitmapHolder holder = new BitmapHolder();

    static BitmapHolder getInstance() {
        return holder;
    }

    Bitmap getBlurredBitmap() {
        if (bitmap == null) return null;

        Bitmap shrinkedBitmap = Bitmap.createScaledBitmap(
                bitmap,
                bitmap.getWidth()/2,
                bitmap.getHeight()/2,
                false);

        return Bitmap.createScaledBitmap(
                shrinkedBitmap,
                shrinkedBitmap.getWidth()*2,
                shrinkedBitmap.getHeight()*2,
                false);
    }

    byte[] getByteArray(){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] bitmapdata = null;
        if(bitmap != null && bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream))
            bitmapdata = stream.toByteArray();

        return bitmapdata;
    }
}
