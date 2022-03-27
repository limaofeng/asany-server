package cn.asany.storage.api;

import java.io.IOException;

public class UploadException extends IOException {

  public UploadException(String message) {
    super(message);
  }
}
