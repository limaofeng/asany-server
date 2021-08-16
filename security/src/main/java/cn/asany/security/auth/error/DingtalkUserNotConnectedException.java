package cn.asany.security.auth.error;

import java.util.Map;
import org.jfantasy.framework.error.ValidationException;

public class DingtalkUserNotConnectedException extends ValidationException {

  public DingtalkUserNotConnectedException(String message, Map<String, Object> data) {
    super(message, data);
  }
}
