package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import controller.FrontController;
import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserCreateService;
import util.HttpRequestUtils;

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

            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            HttpRequest request = new HttpRequest(br);
            String url = request.getUrl();
            String method = request.getMethod();
            Map<String, String> header = request.getHeaders();
            Map<String, String> parameters = request.getParameters();
            String requestBody = request.getBody();

            log.info("url : {}", url);
            log.info("requestBody : {}", requestBody);
            log.info("methodType : {}", method);

            HttpResponse response = new HttpResponse(out);
            FrontController controller = new FrontController();
            controller.service(request, response);

            response.forward(url);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
