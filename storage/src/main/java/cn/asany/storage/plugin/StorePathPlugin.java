package cn.asany.storage.plugin;

import cn.asany.storage.api.*;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.springframework.stereotype.Component;

/**
 * 存储路径生成
 *
 * @author limaofeng
 */
@Slf4j
@Component
public class StorePathPlugin implements StoragePlugin {

  public static final String ID = "store-path";

  @Override
  public String id() {
    return ID;
  }

  @Override
  public boolean supports(UploadContext context) {
    return StringUtil.isBlank(context.getStorePath());
  }

  @Override
  public FileObject upload(UploadContext context, Invocation invocation) throws UploadException {
    String rootFolder = context.getRootFolder();
    UploadFileObject file = context.getFile();

    String mimeType;
    if (!file.isNoFile()) {
      mimeType = FileUtil.getMimeType(file.getFile());
    } else {
      mimeType = StringUtil.defaultValue(file.getMimeType(), () -> "application/octet-stream");
    }

    String extension = FileUtil.getExtension(mimeType);

    String folder =
        rootFolder
            + DateUtil.format("yyyyMMdd")
            + FileObject.SEPARATOR
            + mimeType
            + FileObject.SEPARATOR;

    String filename = StringUtil.shortUUID() + extension;

    context.setStorePath(folder + filename);

    return invocation.invoke();
  }
}
