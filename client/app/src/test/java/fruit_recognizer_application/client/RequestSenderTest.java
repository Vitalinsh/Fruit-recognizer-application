package fruit_recognizer_application.client;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestSenderTest {

    //private static MockWebServer server;
    private static Bitmap correctBitmap, badBitmap;
    private static RequestSender requestSender;
    private static final String FRUIT_NAME = "fruit";

    @Before
    public void setUp(){
//        MockWebServer server = new MockWebServer();
//        MockResponse response200 = new MockResponse();
//        MockResponse response400 = new MockResponse();
//        response200.setResponseCode(200);
//        response200.setBody(FRUIT_NAME);
//        response400.setResponseCode(400);
//        response200.setBody(FRUIT_NAME);
//        server.enqueue(response200);
//        server.enqueue(response400);

//        requestSender = new RequestSender(server.url("/api").toString());
        correctBitmap = mock(Bitmap.class);
        badBitmap = mock(Bitmap.class);
        when(correctBitmap.compress(any(Bitmap.CompressFormat.class), anyInt(), any(ByteArrayOutputStream.class)))
                .thenReturn(true);
        when(badBitmap.compress(any(Bitmap.CompressFormat.class), anyInt(), any(ByteArrayOutputStream.class)))
                .thenReturn(false);
    }

    @Test
    public void requestRecognition_Correct(){
        MockWebServer server = new MockWebServer();
        MockResponse response200 = new MockResponse();
        response200.setResponseCode(200);
        response200.setBody("{ \"message\":\"" + FRUIT_NAME + "\"}");
        server.enqueue(response200);
        requestSender = new RequestSender(server.url("/").toString());
//        Handler mainHandler = new Handler(Looper.getMainLooper());
//        Runnable myRunnable = new Runnable() {
//            @Override
//            public void run() {
//                assertEquals(requestSender.requestRecognition("api", correctBitmap), "azazaz");
//            }
//        };
//        mainHandler.post(myRunnable);
    }

    @Test
    public void requestRecognition_BadImage(){
        assertEquals(requestSender.requestRecognition("", badBitmap), RequestSender.BAD_IMAGE_ERROR_MESSAGE);
    }

    @Test
    public void requestRecognition_onServerFault(){
        MockWebServer server = new MockWebServer();
        MockResponse response400 = new MockResponse();
        response400.setResponseCode(400);
        server.enqueue(response400);
        requestSender = new RequestSender(server.url("/").toString());
        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                assertEquals(requestSender.requestRecognition("api", correctBitmap), "Error ");
            }
        };
        mainHandler.post(myRunnable);
    }

    @Test
    public void bitmapToByteArray_Correct(){
        assertNotNull(RequestSender.bitmapToByteArray(correctBitmap));
    }

    @Test
    public void bitmapToByteArray_Failure(){
        assertNull(RequestSender.bitmapToByteArray(badBitmap));
    }
}
