package webserver;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.Map;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private DataOutputStream dos = null;
    private String firstLine = "HTTP/1.1";
    private int statusCode;
    private String reasonPhrase;
    private String responseBody;
    private String redirectUrl;
    private Map<String, String> headers;


    private void setFirstLine(int statusCode, String reasonPhrase) {
        String firstLine = "HTTP/1.1" + " " + statusCode + " " + reasonPhrase;
        this.firstLine = firstLine;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public HttpResponse() {
    }

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;

    }

    public void addHeader(String key, String value) {
        if(!Strings.isNullOrEmpty(key)) {
            this.headers.put(key, value);
        }
    }

    private void setResponseHeader() throws IOException {
        StringBuilder sb = new StringBuilder();
        for (String key : headers.keySet()) {
            sb.append("key").append(": ").append(headers.get(key)).append("\r\n");
        }
        dos.writeBytes(sb.toString());
    }
    public void response200Header(DataOutputStream dos, int lengthOfBodyContent, String url) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + IOUtils.getContentType(url) + "; charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void response302HeaderWithCookie(DataOutputStream dos, String redirectUrl, boolean loginStatus) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Location: http://localhost:9090/" + redirectUrl + "\r\n");
            dos.writeBytes("Set-Cookie: logined=" + loginStatus + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Location: http://localhost:9090/index.html\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void forward(String url) throws IOException {

        byte[] readData = Files.readAllBytes(new File("./webapp" + url).toPath());
        String contentType = IOUtils.getContentType(url);
        setFirstLine(200, "OK");
        headers.put("Content-Length", String.valueOf(readData.length));
        headers.put("Content-Type", contentType);
        dos.writeBytes(firstLine);
        setResponseHeader();
        responseBody(readData);
    }

    public void forwardBody(String url, String body) throws IOException {
        byte[] readData = body.getBytes();
        String contentType = IOUtils.getContentType(url);
        setFirstLine(200, "OK");
        headers.put("Content-Length", String.valueOf(readData.length));
        headers.put("Content-Type", contentType);
        dos.writeBytes(firstLine);
        setResponseHeader();
        responseBody(readData);
    }

    public void sendRedirect(OutputStream out, String redirectUrl) throws IOException {
        StringBuilder sb = new StringBuilder();

        setFirstLine(302, "FOUND");
        addHeader("Location", redirectUrl);

        dos.writeBytes(firstLine + "\r\n");
        setResponseHeader();
    }


}
