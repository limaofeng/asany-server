package cn.asany.storage.plugin;

import cn.asany.storage.api.*;
import cn.asany.storage.core.StorageResolver;
import cn.asany.storage.core.engine.virtual.VirtualFileObject;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.service.FileService;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Component;

@Component
public class MatchFolderPlugin implements StoragePlugin {

  public static final String ID = "match-folder";

  private final FileService fileService;
  private final StorageResolver storageResolver;

  public MatchFolderPlugin(FileService fileService, StorageResolver storageResolver) {
    this.fileService = fileService;
    this.storageResolver = storageResolver;
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
    Storage storage = context.getStorage();
    StorageSpace space = context.getSpace();

    String name = DateUtil.format("yyyyMMdd");
    FileDetail fileDetail = fileService.getFolderOrCreateIt(name, rootFolder.getId());
    String storageId = fileDetail.getStorageConfig().getId();

    context.setFolder(fileDetail.toFileObject(space));
    context.setStorage(storageResolver.resolve(storageId));

    return invocation.invoke();
  }
}
