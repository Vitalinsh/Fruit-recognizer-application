package com.recognitron.fruitrecognizer.client;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RequestSenderTest {

    private static RequestSender requestSender;
    private static MockWebServer server;
    private static String fulluri;
    private static final String PATH = "/api/recognition";
    private static RequestSender.PostResponse responseResult;
    private static final String CORRECT_MEDIA_TYPE = "image/png";

    @Before
    public void setUp(){
        server = new MockWebServer();
        fulluri = server.url(PATH).toString();

        requestSender = new RequestSender("");
    }

    @Test
    public void postWithByteData_NullUrl(){
        responseResult = requestSender
                .postWithByteData(null, new byte[]{0}, "name", "file", CORRECT_MEDIA_TYPE);
        assertNotNull(responseResult);
        assertTrue(!responseResult.isReadable());
    }

    @Test
    public void postWithByteData_NullData(){
        responseResult = requestSender
                .postWithByteData(fulluri, null, "name", "file", CORRECT_MEDIA_TYPE);
        assertNotNull(responseResult);
        assertTrue(!responseResult.isReadable());
    }

    @Test
    public void postWithByteData_NullName(){
        responseResult = requestSender
                .postWithByteData(fulluri, new byte[]{0}, null, "file", CORRECT_MEDIA_TYPE);
        assertNotNull(responseResult);
        assertTrue(!responseResult.isReadable());
    }

    @Test
    public void postWithByteData_NullFilename(){
        responseResult = requestSender
                .postWithByteData(fulluri, new byte[]{0}, "name", null, CORRECT_MEDIA_TYPE);
        assertNotNull(responseResult);
        assertTrue(!responseResult.isReadable());
    }

    @Test
    public void postWithByteData_NullMediaType(){
        responseResult = requestSender
                .postWithByteData(fulluri, new byte[]{0}, "name", "file", null);
        assertNotNull(responseResult);
        assertTrue(!responseResult.isReadable());
    }

    @Test
    public void postWithByteData_BadMediaType(){
        responseResult = requestSender
                .postWithByteData(fulluri, new byte[]{0}, "name", "file", "quick brown fox");
        assertNotNull(responseResult);
        assertTrue(!responseResult.isReadable());
    }

    @Test
    public void postWithByteData_BadUrl(){
        responseResult = requestSender
                .postWithByteData("http://quick brown fox", new byte[]{0}, "name", "file", CORRECT_MEDIA_TYPE);
        assertNotNull(responseResult);
        assertTrue(!responseResult.isReadable());
    }

    @Test
    public void postWithByteData_NotOKResponse(){
        MockResponse response400 = new MockResponse();
        response400.setResponseCode(400);
        server.enqueue(response400);
        responseResult = requestSender
                .postWithByteData(fulluri, new byte[]{0}, "name", "file", CORRECT_MEDIA_TYPE);
        assertNotNull(responseResult);
        assertTrue(!responseResult.isReadable());
        assertEquals("Error 400", responseResult.getMessage());
    }

    @Test
    public void postWithByteData_Correct(){
        MockResponse nullBodyResponse = new MockResponse();
        nullBodyResponse.setResponseCode(200);
        nullBodyResponse.setBody("message");
        server.enqueue(nullBodyResponse);
        responseResult = requestSender
                .postWithByteData(fulluri, new byte[]{0}, "name", "file", CORRECT_MEDIA_TYPE);
        assertNotNull(responseResult);
        assertTrue(responseResult.isReadable());
        assertEquals("message", responseResult.getMessage());
    }

}
