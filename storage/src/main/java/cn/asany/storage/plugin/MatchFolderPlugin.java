package cn.asany.storage.plugin;

import cn.asany.storage.api.*;
import cn.asany.storage.data.service.FileService;
import java.io.File;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Component;

@Component
public class MatchFolderPlugin implements StoragePlugin {

  public static final String ID = "match-folder";

  private final FileService fileService;

  public MatchFolderPlugin(FileService fileService) {
    this.fileService = fileService;
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
    String rootFolder = context.getRootFolder();
    Storage storage = context.getStorage();

    String folder = rootFolder + DateUtil.format("yyyyMMdd") + File.separator;

    context.setFolder(fileService.getFolderOrCreateIt(folder, storage.getId()).toFileObject());

    return invocation.invoke();
  }
}
