package cn.asany.im.utils;

import cn.asany.im.error.OpenIMServerAPIException;

public class OpenIMUtils {

  public static <T> T getData(GeneralResponse<T> responseBody) throws OpenIMServerAPIException {
    if (responseBody.getErrCode() != 0) {
      throw new OpenIMServerAPIException(responseBody.getErrCode(), responseBody.getErrMsg());
    }
    return responseBody.getData();
  }
}
