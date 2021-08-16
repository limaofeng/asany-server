package cn.asany.security.core.exception;

import org.jfantasy.framework.error.ValidationException;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-06-26 09:48
 */
public class UserNotFoundException extends ValidationException {

  public UserNotFoundException(String message) {
    super("100202", message);
  }
}
