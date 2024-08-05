package webserver;

import com.google.common.base.Strings;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserCreateService;
import util.IOUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.NoSuchElementException;

public class UrlDispatcher {

    private static String DOMAIN = "http://kun-webservice.site:" + 8080;
    public UserCreateService userCreateService = new UserCreateService();
    private static final Logger log = LoggerFactory.getLogger(UrlDispatcher.class);

    public byte[] disaptch(String url) throws IOException {

        if(url.contains("/user/create")){

            String fullPath = url.replace(DOMAIN, "");
            String queryString = fullPath.substring(fullPath.indexOf("?") + 1, fullPath.length());

            try {
                User user = userCreateService.createUser(queryString);
                log.debug("#### crate user Success. ID : {} | password : {} | name : {} | email : {} ####", user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
                return ("create user Success." + user).getBytes();
            } catch(NoSuchElementException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                return ("create user failed. Error: " + e.getMessage()).getBytes();
            }
        }
        return Files.readAllBytes(new File("./webapp" + url).toPath());
    }

    private boolean hasParameters(String url) {
        return url.contains("?");
    }

}
