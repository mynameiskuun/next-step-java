package service;

import com.google.common.base.Strings;
import model.User;
import util.HttpRequestUtils;

import java.util.Map;
import java.util.NoSuchElementException;

public class UserCreateService {

    public User createUser(String params) {

        Map<String, String> parameters = HttpRequestUtils.parseQueryString(params);

        for (String key : parameters.keySet()) {
            String value = parameters.get(key);
            if (Strings.isNullOrEmpty(value)) {
                throw new NoSuchElementException(key + "를 입력 해 주세요");
            }
        }

        return new User(parameters.get("userId"),
                parameters.get("password"),
                parameters.get("name"),
                parameters.get("email"));
    }

}
