package cn.asany.security.auth.error;

import org.jfantasy.framework.error.ValidationException;

import java.util.Map;

public class DingtalkUserNotFoundException extends ValidationException {

    public DingtalkUserNotFoundException(String message, Map<String, Object> data) {
        super(message, data);
    }

}