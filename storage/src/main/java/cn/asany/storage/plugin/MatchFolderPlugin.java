package cn.asany.storage.plugin;

import cn.asany.storage.api.*;
import java.io.File;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Component;

@Component
public class MatchFolderPlugin implements StoragePlugin {

  public static final String ID = "match-folder";

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

    String folder = rootFolder + DateUtil.format("yyyyMMdd") + File.separator;

    context.setFolder(folder);

    return invocation.invoke();
  }
}
