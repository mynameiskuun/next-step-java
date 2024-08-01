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

public class UrlDispatcher {

    private static String DOMAIN = "http://kun-webservice.site:" + 8080;
    public UserCreateService userCreateService = new UserCreateService();
    private static final Logger log = LoggerFactory.getLogger(UrlDispatcher.class);

    public byte[] disaptch(String url) throws IOException {

        if(url.contains("/user/create")){

            String fullPath = url.replace(DOMAIN, "");
            String parameters = fullPath.substring(fullPath.indexOf("?") + 1, fullPath.length());

            User user = userCreateService.createUser(parameters);
            log.debug("#### crate user Success. ID : {} | password : {} | name : {} | email : {} ####", user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
            return ("create user Success." + user.toString()).getBytes();
        }
        return Files.readAllBytes(new File("./webapp" + url).toPath());
    }

    public String extractUrl(String input) {

        return IOUtils.getUrlFromFirstLine(input);
    }

    private boolean hasParameters(String url) {
        return url.contains("?");
    }

}
