package fruit_recognizer_application.client;

import android.graphics.Bitmap;
import com.loopj.android.http.*;
import cz.msebera.android.httpclient.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

class RequestSender {
    private String base_url;
    private AsyncHttpClient client;
    static final String BAD_IMAGE_ERROR_MESSAGE = "Unable to proceed: bad image data";

    RequestSender(String base_url){
        this.base_url = base_url;
        client = new AsyncHttpClient();
    }

    String requestRecognition(String url, Bitmap image){
        byte[] imageData = bitmapToByteArray(makeBitmapSquare(image));
        if(imageData == null)
            return BAD_IMAGE_ERROR_MESSAGE;

        RequestParams params = new RequestParams();
        params.put("image", new ByteArrayInputStream(imageData));
        ResponseHandler responseHandler = new ResponseHandler();
        client.post(base_url + url, params, responseHandler);
        return responseHandler.getStringResponse();
    }

    static byte[] bitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] bitmapdata = null;
        if(bitmap != null && bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream))
            bitmapdata = stream.toByteArray();

        return bitmapdata;
    }

    static Bitmap makeBitmapSquare(Bitmap bitmap){
        if(bitmap.getWidth() > bitmap.getHeight())
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

class ResponseHandler extends JsonHttpResponseHandler {
    private String res = "";

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
            res = response.getString("message");
        } catch (JSONException e) {
            res = "Wrong response format";
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
        res = "Error " + statusCode;
    }

    String getStringResponse(){
        return res;
    }
}
