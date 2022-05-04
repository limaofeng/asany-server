package cn.asany.security.core.exception;

/**
 * 用户不存在
 *
 * @author limaofeng
 */
public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String message) {
    super(message);
  }
}
