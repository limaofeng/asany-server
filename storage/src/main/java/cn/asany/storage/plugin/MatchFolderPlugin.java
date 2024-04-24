package cn.asany.storage.plugin;

import cn.asany.storage.api.*;
import cn.asany.storage.core.StorageResolver;
import cn.asany.storage.core.engine.virtual.VirtualFileObject;
import cn.asany.storage.data.domain.FileDetail;
import cn.asany.storage.data.domain.MultipartUpload;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.data.service.MultipartUploadService;
import cn.asany.storage.data.util.IdUtils;
import net.asany.jfantasy.framework.util.common.DateUtil;
import net.asany.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Component;

@Component
public class MatchFolderPlugin implements StoragePlugin {

  public static final String ID = "match-folder";

  private final FileService fileService;
  private final StorageResolver storageResolver;
  private final MultipartUploadService multipartUploadService;

  public MatchFolderPlugin(
      FileService fileService,
      StorageResolver storageResolver,
      MultipartUploadService multipartUploadService) {
    this.fileService = fileService;
    this.storageResolver = storageResolver;
    this.multipartUploadService = multipartUploadService;
  }

  @Override
  public String id() {
    return ID;
  }

  @Override
  public boolean supports(UploadContext context) {
    return StringUtil.isBlank(context.getFolder())
        && StringUtil.isBlank(context.getOptions().getFolder());
  }

  @Override
  public FileObject upload(UploadContext context, Invocation invocation) throws UploadException {
    VirtualFileObject rootFolder = (VirtualFileObject) context.getRootFolder();
    UploadOptions options = context.getOptions();
    StorageSpace space = context.getSpace();

    if (!context.isMultipartUpload()) {
      String name = DateUtil.format("yyyyMMdd");
      FileDetail fileDetail = fileService.getFolderOrCreateIt(name, rootFolder.getId());
      String storageId = fileDetail.getStorageConfig().getId();

      context.setFolder(fileDetail.toFileObject(space));
      context.setStorage(storageResolver.resolve(storageId));
    } else {
      String multipartUploadId = options.getUploadId();
      MultipartUpload multipartUpload =
          this.multipartUploadService.get(IdUtils.parseUploadId(multipartUploadId));

      context.setStorage(storageResolver.resolve(multipartUpload.getStorage()));
    }

    return invocation.invoke();
  }
}
