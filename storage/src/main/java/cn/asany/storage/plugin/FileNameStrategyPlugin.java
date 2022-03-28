package cn.asany.storage.plugin;

import cn.asany.storage.api.*;
import java.util.UUID;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.web.WebUtil;
import org.springframework.stereotype.Component;

@Component
public class FileNameStrategyPlugin implements StoragePlugin {

  public static final String ID = "file-name-strategy";

  @Override
  public String id() {
    return ID;
  }

  @Override
  public boolean supports(UploadContext context) {
    return StringUtil.isBlank(context.getFilename());
  }

  @Override
  public FileObject upload(UploadContext context, Invocation invocation) throws UploadException {
    UploadOptions options = context.getOptions();
    UploadFileObject object = context.getFile();

    UploadFileNameStrategy fileNameStrategy =
        ObjectUtil.defaultValue(options.getNameStrategy(), () -> UploadFileNameStrategy.UUID);

    String extension = WebUtil.getExtension(object.getName());

    String filename = object.getName();
    if (fileNameStrategy == UploadFileNameStrategy.ORIGINAL) {
      filename = object.getName();
    } else if (fileNameStrategy == UploadFileNameStrategy.UUID) {
      filename =
          StringUtil.hexTo64("0" + UUID.randomUUID().toString().replaceAll("-", ""))
              + (StringUtil.isNotBlank(extension) ? "." + extension : 0);
    }

    context.setFilename(filename);
    return invocation.invoke();
  }
}
