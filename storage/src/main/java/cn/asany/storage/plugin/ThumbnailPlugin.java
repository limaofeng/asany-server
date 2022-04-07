package cn.asany.storage.plugin;

import cn.asany.storage.api.*;
import org.springframework.stereotype.Component;

@Component
public class ThumbnailPlugin implements StoragePlugin {

  @Override
  public String id() {
    return "thumbnail";
  }

  @Override
  public boolean supports(UploadContext context) {
    return true;
  }

  @Override
  public FileObject upload(UploadContext context, Invocation invocation) throws UploadException {

    // 异步生成缩略图

    // 标识不在清理临时文件

    return invocation.invoke();
  }
}
