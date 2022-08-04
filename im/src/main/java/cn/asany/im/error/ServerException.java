package cn.asany.im.error;

import lombok.Getter;

@Getter
public class ServerException extends RuntimeException {

  public ServerException(String message) {
    super(message);
  }
}
