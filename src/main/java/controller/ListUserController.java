package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import util.HttpRequestUtils;

import java.io.IOException;
import java.util.Map;

public class ListUserController extends FrontController{

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {

        StringBuilder sb = new StringBuilder();
        Map<String, String> header = request.getHeaders();
        String url = request.getUrl();

        sb.setLength(0);
        String cookies = header.get("Cookie");
        Map<String, String> cookieMap = HttpRequestUtils.parseCookies(cookies);

        if(Boolean.parseBoolean(cookieMap.get("logined"))) {
            sb.append("\r\n");
            sb.append("#### 사용자 목록 ####\r\n");
            DataBase.findAll().stream().forEach(u -> {
                sb.append("id : ").append(u.getUserId()).append(" | name : ").append(u.getName()).append("\r\n");
            });

            response.forwardBody(url, sb.toString());
            return;
        } else {
            response.addHeader("logined", String.valueOf(Boolean.parseBoolean(cookieMap.get("logined"))));
            response.sendRedirect("/user/login.html");
        }
    }
}
