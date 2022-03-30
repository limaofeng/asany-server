package cn.asany.storage.plugin;

import cn.asany.storage.api.*;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.utils.UploadUtils;
import java.io.File;
import lombok.SneakyThrows;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.springframework.stereotype.Component;

@Component
public class BaseStoragePlugin implements StoragePlugin {

  public static String ID = "upload";

  private final FileService fileService;

  public BaseStoragePlugin(FileService fileService) {
    this.fileService = fileService;
  }

  @Override
  public String id() {
    return ID;
  }

  @Override
  public boolean supports(UploadContext context) {
    return !context.getOptions().isMultipartUpload();
  }

  @Override
  @SneakyThrows
  public FileObject upload(UploadContext context, Invocation invocation) {
    String storePath = context.getStorePath();
    UploadOptions options = context.getOptions();
    String rootFolder = context.getRootFolder();
    UploadFileObject uploadFile = context.getFile();
    Storage storage = context.getStorage();

    FileObject folder = context.getFolder();
    String filename = context.getFilename();

    File file = uploadFile.getFile();

    String md5 = UploadUtils.md5(file);
    String mimeType = FileUtil.getMimeType(file);
    String extension = FileUtil.getExtension(mimeType);

    storage.writeFile(storePath, file);
    FileDetail detail =
        fileService.saveFileDetail(
            folder.getPath() + StringUtil.uuid() + extension,
            filename,
            ObjectUtil.defaultValue(mimeType, uploadFile.getMimeType()),
            uploadFile.getSize(),
            md5,
            storage.getId(),
            storePath,
            "");
    return detail.toFileObject();
  }
}
