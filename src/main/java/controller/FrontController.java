package controller;

import http.HttpRequest;
import http.HttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FrontController implements Controller {
    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {

        String url = request.getUrl();
        String method = request.getMethod();
        Map<String, Controller> controllerMap = new HashMap<>();

        controllerMap.put("/user/create", new CreateUserController());
        controllerMap.put("/user/login", new LoginController());
        controllerMap.put("/user/list", new ListUserController());

        Controller controller = controllerMap.get(url);
        if (controller != null) {
            controller.service(request, response);
        }

        if (method.equals("GET")) {
            doGet(request, response);
        } else if (method.equals("POST")) {
            doPost(request, response);
        }

    }

    public void doGet(HttpRequest request, HttpResponse response) {

    }

    public void doPost(HttpRequest request, HttpResponse response) {

    }
}
