package cn.asany.im.error;

import lombok.Getter;

@Getter
public class OpenIMServerAPIException extends Exception {
  private final int code;

  public OpenIMServerAPIException(int code, String message) {
    super(message);
    this.code = code;
  }
}
