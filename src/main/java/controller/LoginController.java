package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import util.HttpRequestUtils;

import java.io.IOException;
import java.util.Map;

public class LoginController extends FrontController{

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {

        String requestBody = request.getBody();

        Map<String, String> params = HttpRequestUtils.parseQueryString(requestBody);
        String id = params.get("userId");
        String pw = params.get("password");

        User loginUser = DataBase.findUserById(id);
        boolean loginStatus = loginUser != null && loginUser.getPassword().equals(pw);

        String redirectUrl = "";
        if (loginStatus) {
            redirectUrl = "/index.html";
        } else {
            redirectUrl = "/user/login_failed.html";
        }
        response.sendRedirect(redirectUrl);
    }
}
