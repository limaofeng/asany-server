package cn.asany.storage.api;

import java.io.IOException;

public class UploadException extends IOException {

  public UploadException(String message) {
    super(message);
  }

  public UploadException(String message, Exception e) {
    super(message, e);
  }
}
