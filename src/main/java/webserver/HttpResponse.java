package webserver;

import com.google.common.base.Strings;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class HttpResponse {

    DataOutputStream out;
    private String firstLine = "HTTP/1.1";
    private int statusCode;
    private String reasonPhrase;
    private String redirectUrl;
    private Map<String, String> headers;


    private void setFirstLine() {
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

    public HttpResponse(DataOutputStream out) {
        this.out = out;
    }

    public void addHeader(String key, String value) {
        if(!Strings.isNullOrEmpty(key)) {
            this.headers.put(key, value);
        }
    }

    private String setResponseHeader() {
        StringBuilder sb = new StringBuilder();
        for (String key : headers.keySet()) {
            sb.append(firstLine).append("\r\n");
            sb.append("key").append(": ").append(headers.get(key)).append("\r\n");
        }
        return sb.toString();
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

    public void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void forward() {
        out.writeBytes();
    }

    public void sendRedirect() {
        this.statusCode = 302;
        this.reasonPhrase = "FOUND";
        this.firstLine = this.firstLine + " " + statusCode + " " + reasonPhrase;
    }


}
