package cn.asany.security.auth.error;

import java.util.Map;
import org.jfantasy.framework.error.ValidationException;

public class DingtalkUserNotFoundException extends ValidationException {

  public DingtalkUserNotFoundException(String message, Map<String, Object> data) {
    super(message, data);
  }
}
