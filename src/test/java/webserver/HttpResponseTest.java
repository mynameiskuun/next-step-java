package webserver;

import http.HttpResponse;
import org.junit.Test;

import java.io.*;

public class HttpResponseTest {

    private final String testPath = "./src/test/resources/";

    @Test
    public void redirectTest() throws IOException {
        OutputStream out = createOutputStream("Http_GET_Response.txt");

        HttpResponse response = new HttpResponse(out);
        response.sendRedirect("index.html");
    }

    @Test
    public void responseBodyTest() throws IOException {
        OutputStream out = createOutputStream("Http_POST_Response.txt");

        HttpResponse response = new HttpResponse(out);
        response.forwardBody("index.html", out.toString());
    }

    OutputStream createOutputStream(String fileName) throws FileNotFoundException {
        return new FileOutputStream(new File(testPath + fileName));
    }
}
