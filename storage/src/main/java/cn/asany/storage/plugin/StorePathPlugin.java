package cn.asany.storage.plugin;

import cn.asany.storage.api.*;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.jfantasy.framework.util.web.WebUtil;
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

    String mimeType =
        StringUtil.defaultValue(
            file.getMimeType(),
            () ->
                file.isNoFile()
                    ? "application/octet-stream"
                    : FileUtil.getMimeType(file.getFile()));

    String extension = WebUtil.getExtension(file.getName());

    String folder =
        rootFolder
            + DateUtil.format("yyyyMMdd")
            + FileObject.SEPARATOR
            + mimeType
            + FileObject.SEPARATOR;

    String filename =
        StringUtil.shortUUID() + (StringUtil.isNotBlank(extension) ? "." + extension : "");

    context.setStorePath(folder + filename);

    return invocation.invoke();
  }
}
