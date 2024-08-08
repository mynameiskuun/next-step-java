package util;

import com.google.common.base.Strings;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

public class IOUtils {
    /**
     * @param BufferedReader는
     *            Request Body를 시작하는 시점이어야
     * @param contentLength는
     *            Request Header의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    public static String getContentType(String url) {

        if (url.contains(".html")) {
            return "text/html;charset=utf-8";
        } else if (url.contains(".css")) {
            return "text/css";
        } else if (url.contains(".js")) {
            return "application/javascript";
        } else if (url.contains(".png")) {
            return "image/png";
        } else if (url.contains(".jpg") || url.contains(".jpeg")) {
            return "image/jpeg";
        } else if (url.contains(".gif")) {
            return "image/gif";
        } else {
            return "text/plain";
        }
    }


    public static String getMethodTypeFromHeader(String header) {

        String firstLine = header.split("\r\n")[0];
        return firstLine.split(" ")[0];
    }
    public static String getUrlFromHeader(String header) {

        String firstLine = header.split("\r\n")[0];
        if (Strings.isNullOrEmpty(firstLine)) {
            return "";
        }
        String[] inputArray = firstLine.split(" ");
        return inputArray[1];
    }

    public static String readHeaders(BufferedReader br, StringBuilder sb) throws IOException {

        String header = "";
        while((header = br.readLine()) != null && !header.isEmpty()) {
            sb.append(header).append("\r\n");
        }
        return sb.toString();
    }

    public static String readResponseBody(BufferedReader br, StringBuilder sb, String header) throws IOException {

        String contentLengthStr = getValueFromHeader(header, "Content-Length");
        if(contentLengthStr.isEmpty()) {
            return "";
        }

        int contentLength = Integer.parseInt(contentLengthStr);


        char[] bodyChars = new char[contentLength];
        int totalRead = 0;

        //String responseBody = "";
        while (totalRead < contentLength) {
            int charsRead = br.read(bodyChars, totalRead, contentLength - totalRead);
            if(charsRead == -1) {
                break;
            }
            totalRead += charsRead;
        }
        return new String(bodyChars, 0, totalRead);
    }

    public static String getHostFromHeader(String header) {

        String secondLine = header.split("\r\n")[1];
        if (Strings.isNullOrEmpty(secondLine)) {
            return "";
        }
        String[] inputArray = secondLine.split(" ");
        return inputArray[1];
    }

    public static String getValueFromHeader(String header, String target) {
        String[] headerArr = header.split("\r\n");
        String targetHeaderLine = Arrays.stream(headerArr)
                .filter(line -> line.startsWith(target + ":"))
                .findFirst()
                .orElse("");
        return Strings.isNullOrEmpty(targetHeaderLine) ? "" : targetHeaderLine.split(": ", 2)[1].trim();
    }



}
