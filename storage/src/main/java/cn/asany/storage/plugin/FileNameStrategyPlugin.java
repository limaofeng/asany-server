package cn.asany.storage.plugin;

import cn.asany.storage.api.*;
import cn.asany.storage.data.service.FileService;
import java.util.UUID;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.framework.util.common.StringUtil;
import net.asany.jfantasy.framework.util.ognl.OgnlUtil;
import net.asany.jfantasy.framework.util.regexp.RegexpUtil;
import net.asany.jfantasy.framework.util.web.WebUtil;
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
    String name = RegexpUtil.replace(filename, "(" + extension + ")$", "");

    Long folderId = OgnlUtil.getInstance().getValue("id", folder);

    if (fileNameStrategy == UploadFileNameStrategy.ORIGINAL) {
      filename = object.getName();
      int index = 1;
      while (fileService.exists(filename, folderId, storage.getId())) {
        filename = name + "_" + index + extension;
        index++;
      }
    } else if (fileNameStrategy == UploadFileNameStrategy.UUID) {
      filename =
          StringUtil.hexTo64("0" + UUID.randomUUID().toString().replaceAll("-", "")) + extension;
    }

    context.setFilename(filename);
    return invocation.invoke();
  }
}
