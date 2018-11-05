package fruit_recognizer_application.client;

import android.graphics.Bitmap;
import com.google.gson.JsonParser;
import okhttp3.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

class RequestSender {
    private String base_url;
    private OkHttpClient client;
    static final String BAD_IMAGE_ERROR_MESSAGE = "Unable to proceed: bad image data";

    RequestSender(String base_url){
        this.base_url = base_url;
        client = new OkHttpClient();
    }

    String requestRecognition(String url, Bitmap image) {
        byte[] imageData = bitmapToByteArray(makeBitmapSquare(image));
        if(imageData == null)
            return BAD_IMAGE_ERROR_MESSAGE;

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "", RequestBody.create(MediaType.parse("image/*png"), imageData))
                .build();
        Request request = new Request.Builder()
                .url(base_url + url)
                .post(body)
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            return "Server Unavailable";
        }
        try {
            if(response.code() != 200)
                return "Error " + response.code();
            return response.body() != null ? new JsonParser().parse(response.body().string())
                                        .getAsJsonObject()
                                        .getAsJsonPrimitive("message")
                                        .getAsString(): null;
        } catch (IOException e) {
            return "Unable to read response";
        }
    }

    static byte[] bitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] bitmapdata = null;
        if(bitmap != null && bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream))
            bitmapdata = stream.toByteArray();

        return bitmapdata;
    }

    static Bitmap makeBitmapSquare(Bitmap bitmap){
        if(bitmap == null) return null;
        else if(bitmap.getHeight() == bitmap.getWidth()) return bitmap;
        else if(bitmap.getWidth() > bitmap.getHeight())
            return Bitmap.createBitmap(bitmap,
                    bitmap.getWidth()/2 - bitmap.getHeight()/2,
                    0,
                    bitmap.getHeight(),
                    bitmap.getHeight());
        else
            return Bitmap.createBitmap(bitmap,
                    0,
                    bitmap.getHeight()/2 - bitmap.getWidth()/2,
                    bitmap.getWidth(),
                    bitmap.getWidth());
    }
}
