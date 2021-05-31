package cn.asany.security.auth.error;

import org.jfantasy.framework.error.ValidationException;

import java.util.Map;

public class DingtalkServiceException extends ValidationException {

    public DingtalkServiceException(String message, Map<String, Object> data) {
        super(message, data);
    }

}
