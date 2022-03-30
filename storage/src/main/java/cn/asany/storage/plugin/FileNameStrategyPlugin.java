package cn.asany.storage.plugin;

import cn.asany.storage.api.*;
import cn.asany.storage.data.service.FileService;
import java.util.UUID;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.web.WebUtil;
import org.springframework.stereotype.Component;

@Component
public class FileNameStrategyPlugin implements StoragePlugin {

  public static final String ID = "file-name-strategy";

  private final FileService fileService;

  public FileNameStrategyPlugin(FileService fileService) {
    this.fileService = fileService;
  }

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
    Storage storage = context.getStorage();
    FileObject folder = context.getFolder();

    UploadFileNameStrategy fileNameStrategy =
        ObjectUtil.defaultValue(options.getNameStrategy(), () -> UploadFileNameStrategy.ORIGINAL);

    String extension = WebUtil.getExtension(object.getName());

    String filename = object.getName();
    String name =
        extension.length() > 0
            ? filename.substring(0, object.getName().length() - extension.length() - 1)
            : filename;

    if (fileNameStrategy == UploadFileNameStrategy.ORIGINAL) {
      filename = object.getName();
      int index = 1;
      while (fileService.exists(folder.getPath() + filename, storage.getId())) {
        filename = name + "_" + index + "." + extension;
      }
    } else if (fileNameStrategy == UploadFileNameStrategy.UUID) {
      filename =
          StringUtil.hexTo64("0" + UUID.randomUUID().toString().replaceAll("-", ""))
              + (StringUtil.isNotBlank(extension) ? "." + extension : 0);
    }

    context.setFilename(filename);
    return invocation.invoke();
  }
}
