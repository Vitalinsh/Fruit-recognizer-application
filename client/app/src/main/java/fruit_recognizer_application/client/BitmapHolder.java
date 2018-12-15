package fruit_recognizer_application.client;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropSquareTransformation;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;

class BitmapHolder {

    private Bitmap bitmap;

    Bitmap getBitmap() {
        return bitmap;
    }

    void setBitmap(Bitmap bitmap, Context context) throws ExecutionException, InterruptedException {
        Drawable image = GlideApp.with(context)
                .load(bitmap)
                .transforms(new CropSquareTransformation())
                .submit()
                .get();
        this.bitmap = ((BitmapDrawable)image).getBitmap();
    }

    private static final BitmapHolder holder = new BitmapHolder();

    static BitmapHolder getInstance() {
        return holder;
    }

    Bitmap getBlurredBitmap(Context context) throws ExecutionException, InterruptedException {
        Drawable image = GlideApp.with(context)
                    .load(bitmap)
                    .transforms(new BlurTransformation())
                    .submit()
                    .get();
        return ((BitmapDrawable)image).getBitmap();
    }

    byte[] getByteArray(){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] bitmapdata = null;
        if(bitmap != null && bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream))
            bitmapdata = stream.toByteArray();

        return bitmapdata;
    }
}
