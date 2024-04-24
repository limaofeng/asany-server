package cn.asany.security.auth.error;

import java.util.Map;
import net.asany.jfantasy.framework.error.ValidationException;

public class DingtalkServiceException extends ValidationException {

  public DingtalkServiceException(String message, Map<String, Object> data) {
    super(message, data);
  }
}
