package cn.asany.storage.plugin;

import cn.asany.storage.api.*;
import cn.asany.storage.data.util.IdUtils;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Component;

@Component
public class FindFolderPlugin implements StoragePlugin {

  public static final String ID = "find-folder-name";

  @Override
  public String id() {
    return ID;
  }

  @Override
  public boolean supports(UploadContext context) {
    return StringUtil.isBlank(context.getFolder())
        && StringUtil.isNotBlank(context.getOptions().getFolder());
  }

  @Override
  public FileObject upload(UploadContext context, Invocation invocation) throws UploadException {
    UploadOptions options = context.getOptions();

    String rootFolder = context.getRootFolder();

    IdUtils.FileKey fileKey = IdUtils.parseKey(options.getFolder());

    if (!fileKey.getPath().startsWith(rootFolder)) {
      throw new UploadException("文件夹位置错误");
    }

    context.setFolder(fileKey.getPath());

    return invocation.invoke();
  }
}
