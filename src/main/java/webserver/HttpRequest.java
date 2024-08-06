package webserver;

import com.google.common.base.Strings;
import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private String firstLine;
    private String method;
    private String url;
    private String body;
    private Map<String, String> headers;
    private Map<String, String> parameters;

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getBody() {
        return body;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public HttpRequest() {
    }

    public HttpRequest(BufferedReader br) throws IOException {
        this.firstLine = br.readLine();
        this.headers = setHeaders(br);
        this.body = setBody(br);
        this.method = setMethod(firstLine);
        this.url = setUrl(firstLine);
        this.parameters = setParameters(url);
    }

    private String setMethod(String firstLine) {
        return firstLine.split(" ")[0];
    }

    private String setUrl(String firstLine) {
        return firstLine.split(" ")[1];
    }

    private Map<String, String> setParameters(String url) {

        String queryString = null;

        if(this.method.equals("GET")) {
            int startPoint = url.indexOf("?");
            if(startPoint > 0) {
                queryString = url.substring(url.indexOf(startPoint) + 1, url.length());
            }
        }

        if(this.method.equals("POST")) {
            queryString = this.body;
        }
        return HttpRequestUtils.parseQueryString(queryString);
    }

    private Map<String, String> setHeaders(BufferedReader br) throws IOException {
        String headers;
        Map<String, String> headerMap = new HashMap<>();

        while((headers = br.readLine()) != null && !headers.isEmpty()) {
            String key = headers.split(": ")[0];
            String value = headers.split(": ")[1];

            headerMap.put(key, value);
        }

        return headerMap;
    }

    private String setBody(BufferedReader br) throws IOException {
        String contentLengthStr = headers.get("Content-Length");
        if(Strings.isNullOrEmpty(contentLengthStr)) {
            return "";
        }

        int contentLength = Integer.valueOf(contentLengthStr);
        char[] bodyChars = new char[contentLength];
        int totalRead = 0;

        while (totalRead < contentLength) {
            int charsRead = br.read(bodyChars, totalRead, contentLength - totalRead);
            if(charsRead == -1) {
                break;
            }
            totalRead += charsRead;
        }
        return new String(bodyChars, 0, totalRead);
    }
}
