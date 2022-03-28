package cn.asany.storage.plugin;

import cn.asany.storage.api.*;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.utils.UploadUtils;
import java.io.File;
import lombok.SneakyThrows;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.jfantasy.framework.util.web.WebUtil;
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
    UploadOptions options = context.getOptions();
    String rootFolder = context.getRootFolder();
    UploadFileObject uploadFile = context.getFile();
    Storage storage = context.getStorage();

    String folder = context.getFolder();
    String filename = context.getFilename();

    File file = uploadFile.getFile();

    String md5 = UploadUtils.md5(file);
    String mimeType = FileUtil.getMimeType(file);

    String extension = WebUtil.getExtension(uploadFile.getName());

    String absolutePath = folder + filename;

    storage.writeFile(absolutePath, file);
    FileDetail detail =
        fileService.saveFileDetail(
            absolutePath,
            uploadFile.getName(),
            ObjectUtil.defaultValue(mimeType, uploadFile.getMimeType()),
            uploadFile.getSize(),
            md5,
            storage.getId(),
            "");
    return detail.toFileObject();
  }
}
