package test.nz.ac.vuw.swen301.assignment3.server;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class BlackBoxTests {
    private final static String SINGLELOG = "[\"{\\\"id\\\":\\\"96898390-4676-459f-b437-ac10cb4a9320\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.431Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"DEBUG\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\"]";

    private static Process process;
    private static final String TEST_HOST = "localhost";
    private static final int TEST_PORT = 8080;
    private static final String TEST_PATH = "/resthome4logs"; // as defined in pom.xml
    private static final String SERVICE_PATH = TEST_PATH + "/logs"; // as defined in pom.xml and web.xml
    private static final String SERVICE_STATS_PATH = TEST_PATH + "/stats";

    @BeforeClass
    public static void startServer() throws Exception {
        process = Runtime.getRuntime().exec("mvn jetty:run");
        Thread.sleep(5000);
    }

    @AfterClass
    public static void stopServer() throws Exception {
        Runtime.getRuntime().exec("mvn jetty:stop");
        process.destroy();
        Thread.sleep(3000);
    }

    private HttpResponse get(URI uri) throws Exception {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(uri);
        return httpClient.execute(request);
    }

    private boolean isServerReady() throws Exception {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(TEST_PATH);
        URI uri = builder.build();
        try {
            HttpResponse response = get(uri);
            boolean success = response.getStatusLine().getStatusCode() == 200;

            if (!success) {
                System.err.println("Check whether server is up and running, request to " + uri + " returns " + response.getStatusLine());
            }

            return success;
        }
        catch (Exception x) {
            System.err.println("Encountered error connecting to " + uri + " -- check whether server is running and application has been deployed");
            return false;
        }
    }

    @Test
    public void testBadContentTypeDoPost() throws Exception {
        Assume.assumeTrue(isServerReady());
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(SERVICE_PATH);
        URI uri = null;
        try {
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        // http://localhost:8080/resthome4logs
        HttpClient httpClient = HttpClientBuilder.create().build();
        //post request to server
        HttpPost postRequest = new HttpPost(uri);
        postRequest.setHeader("Content-Type", "bad");
        postRequest.setEntity(new ByteArrayEntity(SINGLELOG.getBytes()));
        HttpResponse postResponse = null;
        try {
            postResponse = httpClient.execute(postRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(400, postResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void testDoPostANDDoGet() throws Exception{
        Assume.assumeTrue(isServerReady());
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(SERVICE_PATH);
        URI uri = null;
        try {
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        // http://localhost:8080/resthome4logs
        HttpClient httpClient = HttpClientBuilder.create().build();
        //post request to server
        HttpPost postRequest = new HttpPost(uri);
        postRequest.setHeader("Content-Type", "application/json");
        postRequest.setEntity(new ByteArrayEntity(SINGLELOG.getBytes()));
        HttpResponse postResponse = null;
        try {
            postResponse = httpClient.execute(postRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(201,postResponse.getStatusLine().getStatusCode());

        Assume.assumeTrue(isServerReady());
        builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(SERVICE_PATH)
                .setParameter("level", "DEBUG").setParameter("limit", "1");
        uri = null;
        try {
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        // http://localhost:8080/resthome4logs
        httpClient = HttpClientBuilder.create().build();

        //get request to server
        HttpGet getRequest = new HttpGet(uri);
        HttpResponse getResponse = null;
        try {
            getResponse = httpClient.execute(getRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String getContent = EntityUtils.toString(getResponse.getEntity());
            //System.out.println(getContent);
            assertTrue(getContent.contains("TEST"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(200,getResponse.getStatusLine().getStatusCode());
        assertEquals("application/json", getResponse.getEntity().getContentType().getValue());
    }

    @Test
    public void testDoBADLevelGet() throws Exception{
        Assume.assumeTrue(isServerReady());
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(SERVICE_PATH)
                .setParameter("level", "BAD").setParameter("limit", "1");
        URI uri = null;
        try {
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        // http://localhost:8080/resthome4logs
        HttpClient httpClient = HttpClientBuilder.create().build();

        //get request to server
        HttpGet getRequest = new HttpGet(uri);
        HttpResponse getResponse = null;
        try {
            getResponse = httpClient.execute(getRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String getContent = EntityUtils.toString(getResponse.getEntity());
            //System.out.println(getContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(400,getResponse.getStatusLine().getStatusCode());
    }

    @Test
    public void testDoBADNegativeLimitGet() throws Exception{
        Assume.assumeTrue(isServerReady());
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(SERVICE_PATH)
                .setParameter("level", "DEBUG").setParameter("limit", "-1");
        URI uri = null;
        try {
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        // http://localhost:8080/resthome4logs
        HttpClient httpClient = HttpClientBuilder.create().build();

        //get request to server
        HttpGet getRequest = new HttpGet(uri);
        HttpResponse getResponse = null;
        try {
            getResponse = httpClient.execute(getRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String getContent = EntityUtils.toString(getResponse.getEntity());
            //System.out.println(getContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(400,getResponse.getStatusLine().getStatusCode());
        //assertTrue(getContent.contains("TEST"));
    }

    @Test
    public void testDoBADDecimalLimitGet() throws Exception{
        Assume.assumeTrue(isServerReady());
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(SERVICE_PATH)
                .setParameter("level", "DEBUG").setParameter("limit", "0.5");
        URI uri = null;
        try {
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        // http://localhost:8080/resthome4logs
        HttpClient httpClient = HttpClientBuilder.create().build();

        //get request to server
        HttpGet getRequest = new HttpGet(uri);
        HttpResponse getResponse = null;
        try {
            getResponse = httpClient.execute(getRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String getContent = EntityUtils.toString(getResponse.getEntity());
            //System.out.println(getContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(400,getResponse.getStatusLine().getStatusCode());
        //assertTrue(getContent.contains("TEST"));
    }

    @Test
    public void testDoGetStats() throws Exception{
        Assume.assumeTrue(isServerReady());
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(SERVICE_STATS_PATH)
                .setParameter("statsRequest", "not-null");
        URI uri = null;
        try {
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        // http://localhost:8080/resthome4logs
        HttpClient httpClient = HttpClientBuilder.create().build();

        //get request to server
        HttpGet getRequest = new HttpGet(uri);
        HttpResponse getResponse = null;
        try {
            getResponse = httpClient.execute(getRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(200,getResponse.getStatusLine().getStatusCode());
        assertEquals("application/vnd.ms-excel", getResponse.getEntity().getContentType().getValue());
        //assertTrue(getContent.contains("TEST"));
    }

}
