package cn.asany.storage.plugin;

import cn.asany.storage.api.*;
import cn.asany.storage.core.engine.virtual.VirtualFileObject;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.bean.Space;
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
    return !context.isMultipartUpload();
  }

  @Override
  @SneakyThrows
  public FileObject upload(UploadContext context, Invocation invocation) {
    String storePath = context.getStorePath();
    UploadOptions options = context.getOptions();
    Space space = (Space) context.getSpace();
    UploadFileObject uploadFile = context.getFile();
    Storage storage = context.getStorage();

    VirtualFileObject folder = (VirtualFileObject) context.getFolder();
    String filename = context.getFilename();

    File file = uploadFile.getFile();

    String md5 = UploadUtils.md5(file);
    String mimeType = FileUtil.getMimeType(file);
    String extension = FileUtil.getExtension(mimeType);

    storage.writeFile(storePath, file);
    FileDetail detail =
        fileService.createFile(
            folder.getOriginalPath() + StringUtil.uuid() + extension,
            filename,
            ObjectUtil.defaultValue(mimeType, uploadFile.getMimeType()),
            uploadFile.getSize(),
            md5,
            storage.getId(),
            storePath,
            "",
            folder.getId());
    return detail.toFileObject(space);
  }
}
