package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserCreateService;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            InputStreamReader inputStreamReader = new InputStreamReader(in);

            UrlDispatcher dispatcher = new UrlDispatcher();
            BufferedReader br = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();

            String header = IOUtils.readHeaders(br, sb);
            String url = IOUtils.getUrlFromHeader(header);
            String host = IOUtils.getValueFromHeader(header, "Host");
            String requestBody = IOUtils.readResponseBody(br, sb, header);
            String methodType = IOUtils.getMethodTypeFromHeader(header);

            UserCreateService userCreateService = new UserCreateService();
            DataOutputStream dos = new DataOutputStream(out);

            log.info("url : {}", url);
            log.info("requestBody : {}", requestBody);
            log.info("methodType : {}", methodType);

            User user = null;
            if(methodType.equals("POST") && url.equals("/user/create")) {
                user = userCreateService.createUser(requestBody);
                log.debug("#### crate user Success. ID : {} | password : {} | name : {} | email : {} ####", user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
                byte[] readData = ("create user Success." + user).getBytes();
                DataBase.addUser(user);
                response302Header(dos);
                responseBody(dos, readData);
                return;
            }

            if(methodType.equals("POST") && url.equals("/user/login")) {
                Map<String, String> params = HttpRequestUtils.parseQueryString(requestBody);
                String id = params.get("userId");
                String pw = params.get("password");

                User loginUser = DataBase.findUserById(id);
                boolean loginStatus = loginUser != null && loginUser.getPassword().equals(pw);

                String redirectUrl = "";
                if (loginStatus) {
                    redirectUrl = "index.html";
                } else {
                    redirectUrl = "user/login_failed.html";
                }
                response302HeaderWithCookie(dos, redirectUrl, loginStatus);
            }

            if(url.equals("/user/list")) {
                String cookies = IOUtils.getValueFromHeader(header, "Cookie");
                Map<String, String> cookieMap = HttpRequestUtils.parseCookies(cookies);

                if(Boolean.parseBoolean(cookieMap.get("logined"))) {
                    sb.append("#### 사용자 목록 ####");
                    DataBase.findAll().stream().forEach(u -> {
                        sb.append("id : ").append(u.getUserId()).append("name : ").append(u.getName());
                    });

                    byte[] readData = sb.toString().getBytes();
                    //response302Header(dos);
                    responseBody(dos, readData);
                    return;
                } else {
                    response302HeaderWithCookie(dos, "user/login.html", Boolean.parseBoolean(cookieMap.get("logined")));
                }
            }

            byte[] readData = Files.readAllBytes(new File("./webapp" + url).toPath());

            //byte[] readData = dispatcher.disaptch(url);
            //byte[] readData = Files.readAllBytes(new File("./webapp" + url).toPath());

            response200Header(dos, readData.length, url);
            responseBody(dos, readData);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302HeaderWithCookie(DataOutputStream dos, String redirectUrl, boolean loginStatus) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Location: http://localhost:9090/" + redirectUrl + "\r\n");
            dos.writeBytes("Set-Cookie: logined=" + loginStatus + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Location: http://localhost:9090/index.html\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String url) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + IOUtils.getContentType(url) + "; charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
