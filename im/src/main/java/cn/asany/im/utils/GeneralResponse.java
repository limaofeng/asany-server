package cn.asany.im.utils;

import lombok.Data;

@Data
public class GeneralResponse<T> {
  private int errCode;
  private String errMsg;
  private T data;
}
