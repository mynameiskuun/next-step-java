package webserver;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtilsTest;

import java.io.*;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class HttpRequestTest {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestTest.class);
    private String testDirectory = "./src/test/resources/";

    @Test
    public void HttpGetRequestTest() throws IOException {
        InputStream in = new FileInputStream(new File(testDirectory + "Http_GET.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        HttpRequest httpRequest = new HttpRequest(br);
        String method = httpRequest.getMethod();
        String url = httpRequest.getUrl();
        Map<String, String> headers = httpRequest.getHeaders();
        Map<String, String> parameters = httpRequest.getParameters();

        assertEquals(method, "GET");
        assertEquals(url, "/user/create?userId=testId&password=password&name=sknam");
        assertEquals(headers.get("Host"), "localhost:9090");
        assertEquals(parameters.get("name"), "sknam");
    }

    @Test
    public void HttpPostRequestTest() throws IOException {
        InputStream in = new FileInputStream(new File(testDirectory + "Http_POST.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        HttpRequest httpRequest = new HttpRequest(br);
        String method = httpRequest.getMethod();
        String url = httpRequest.getUrl();
        Map<String, String> headers = httpRequest.getHeaders();
        Map<String, String> parameters = httpRequest.getParameters();

        assertEquals(method, "POST");
        assertEquals(url, "/user/login");
        assertEquals(headers.get("Host"), "localhost:9090");
        assertEquals(parameters.get("id"), "userId");
        assertEquals(parameters.get("password"), "password");
    }
}