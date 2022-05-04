package cn.asany.security.core.exception;

/** 用户状态异常 */
public class UserInvalidException extends RuntimeException {

  public UserInvalidException(String message) {
    super(message);
  }
}
