package cn.asany.storage.plugin;

import cn.asany.storage.api.*;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.utils.UploadUtils;
import java.io.File;
import java.util.UUID;
import lombok.SneakyThrows;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.jfantasy.framework.util.web.WebUtil;
import org.springframework.stereotype.Component;

@Component
public class BaseStoragePlugin implements StoragePlugin {

  private final FileService fileService;

  public BaseStoragePlugin(FileService fileService) {
    this.fileService = fileService;
  }

  @Override
  public boolean supports(UploadContext context) {
    return !context.getOptions().isMultipartUpload();
  }

  @Override
  @SneakyThrows
  public FileObject upload(UploadContext context, Invocation invocation) {
    String location = context.getLocation();
    Storage storage = context.getStorage();
    StorageSpace space = context.getSpace();

    UploadOptions options = context.getOptions();

    String folder = options.getFolder();

    FileObject object = context.getObject();
    File file = context.getFile();

    String md5 = UploadUtils.md5(file);
    String mimeType = FileUtil.getMimeType(file);

    String extension = WebUtil.getExtension(object.getName());
    String filename =
        StringUtil.hexTo64("0" + UUID.randomUUID().toString().replaceAll("-", ""))
            + (StringUtil.isNotBlank(extension) ? "." + extension : 0);
    // 获取虚拟目录
    String absolutePath;

    if (StringUtil.isNotBlank(folder)) {
      if (folder.startsWith("/")) {
        absolutePath = folder.substring(1) + File.separator + filename;
      } else {
        absolutePath = space.getPath() + folder + File.separator + filename;
      }
    } else {
      absolutePath = location + DateUtil.format("yyyyMMdd") + File.separator + filename;
    }

    storage.writeFile(absolutePath, file);
    FileDetail detail =
        fileService.saveFileDetail(
            absolutePath,
            object.getName(),
            ObjectUtil.defaultValue(mimeType, object.getMimeType()),
            object.getSize(),
            md5,
            context.getStorageId(),
            "");
    return detail.toFileObject();
  }
}
