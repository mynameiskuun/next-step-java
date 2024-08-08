package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserCreateService;
import webserver.RequestHandler;

import java.io.IOException;

public class CreateUserController extends FrontController{

    private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {

        UserCreateService userCreateService = new UserCreateService();
        String requestBody = request.getBody();

        User user = userCreateService.createUser(requestBody);
        log.debug("#### crate user Success. ID : {} | password : {} | name : {} | email : {} ####", user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
        byte[] readData = ("create user Success." + user).getBytes();
        DataBase.addUser(user);
        response.sendRedirect("/index.html");
    }
}
