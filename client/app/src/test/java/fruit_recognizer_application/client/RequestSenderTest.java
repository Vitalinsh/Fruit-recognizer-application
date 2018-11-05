package fruit_recognizer_application.client;

import android.graphics.Bitmap;
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

    private static Bitmap correctBitmap, badBitmap;
    private static RequestSender requestSender;
    private static final String FRUIT_NAME = "fruit";

    @Before
    public void setUp(){
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
        assertEquals(FRUIT_NAME, requestSender.requestRecognition("", correctBitmap));
    }

    @Test
    public void requestRecognition_BadImage(){
        assertEquals(requestSender.requestRecognition("", badBitmap), RequestSender.BAD_IMAGE_ERROR_MESSAGE);
    }

    //Nata
    @Test
    public void requestRecognition_onServerFault(){

    }

    @Test
    public void bitmapToByteArray_Correct(){
        assertNotNull(RequestSender.bitmapToByteArray(correctBitmap));
    }

    @Test
    public void bitmapToByteArray_Failure(){
        assertNull(RequestSender.bitmapToByteArray(badBitmap));
    }

    @Test
    public void makeBitmapSquare_Correct(){
        Bitmap bitmap = Bitmap.createBitmap(50, 100, Bitmap.Config.ARGB_8888);
        Bitmap square = RequestSender.makeBitmapSquare(bitmap);
        assertEquals(square.getHeight(), 50);
        assertEquals(square.getWidth(), 50);
        bitmap = Bitmap.createBitmap(100, 50, Bitmap.Config.ARGB_8888);
        square = RequestSender.makeBitmapSquare(bitmap);
        assertEquals(square.getHeight(), 50);
        assertEquals(square.getWidth(), 50);
        bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
        square = RequestSender.makeBitmapSquare(bitmap);
        assertEquals(square.getHeight(), 50);
        assertEquals(square.getWidth(), 50);
    }

    //Nata
    @Test
    public void makeBitmapSquare_Failure(){

    }
}
