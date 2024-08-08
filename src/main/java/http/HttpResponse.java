package http;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private DataOutputStream dos = null;
    private String firstLine = "HTTP/1.1";
    private Map<String, String> headers = new HashMap<>();


    public HttpResponse() {
    }

    public HttpResponse(OutputStream dos) {
        this.dos = new DataOutputStream(dos);
    }

    public void addHeader(String key, String value) {
        if(!Strings.isNullOrEmpty(key)) {
            this.headers.put(key, value);
        }
    }

    public void forward(String url) throws IOException {

        byte[] readData = Files.readAllBytes(new File("./webapp" + url).toPath());
        String contentType = IOUtils.getContentType(url);
        headers.put("Content-Type", contentType);
        headers.put("Content-Length", String.valueOf(readData.length));
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        processHeaders();

        dos.writeBytes("\r\n");
        responseBody(readData);
    }

    public void forwardBody(String url, String body) throws IOException {
        byte[] readData = body.getBytes();
        String contentType = IOUtils.getContentType(url);
        headers.put("Content-Type", contentType);
        headers.put("Content-Length", String.valueOf(readData.length));
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        processHeaders();

        dos.writeBytes("\r\n");
        responseBody(readData);
    }

    public void sendRedirect(String redirectUrl) throws IOException {
        StringBuilder sb = new StringBuilder();

        addHeader("Location", redirectUrl);

        dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
        processHeaders();
    }


    private void processHeaders() throws IOException {
        StringBuilder sb = new StringBuilder();
        for (String key : headers.keySet()) {
            sb.append(key).append(": ").append(headers.get(key)).append("\r\n");
        }
        dos.writeBytes(sb.toString());
    }

    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
