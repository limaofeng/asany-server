package cn.asany.im.error;

import lombok.Getter;

/**
 * OpenIM 接口调用异常
 *
 * @author limaofeng
 */
@Getter
public class OpenIMServerAPIException extends Exception {
  private final int code;

  public OpenIMServerAPIException(int code, String errMsg, String errDlt) {
    super("(" + code + ")" + errMsg + ":" + errDlt);
    this.code = code;
  }
}
