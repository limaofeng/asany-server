package cn.asany.storage.plugin;

import cn.asany.storage.api.*;
import org.springframework.stereotype.Component;

@Component
public class CleanPlugin implements StoragePlugin {

  public static String ID = "clean";

  @Override
  public String id() {
    return ID;
  }

  @Override
  public boolean supports(UploadContext context) {
    return true;
  }

  @Override
  public FileObject upload(UploadContext context, Invocation invocation) throws UploadException {
    return invocation.invoke();
  }
}
