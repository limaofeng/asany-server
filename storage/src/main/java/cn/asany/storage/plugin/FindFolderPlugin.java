/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.storage.plugin;

import cn.asany.storage.api.*;
import cn.asany.storage.core.StorageResolver;
import cn.asany.storage.data.domain.FileDetail;
import cn.asany.storage.data.util.IdUtils;
import net.asany.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Component;

@Component
public class FindFolderPlugin implements StoragePlugin {

  public static final String ID = "find-folder-name";

  private final StorageResolver storageResolver;

  public FindFolderPlugin(StorageResolver storageResolver) {
    this.storageResolver = storageResolver;
  }

  @Override
  public String id() {
    return ID;
  }

  @Override
  public boolean supports(UploadContext context) {
    return StringUtil.isBlank(context.getFolder())
        && StringUtil.isNotBlank(context.getOptions().getFolder());
  }

  @Override
  public FileObject upload(UploadContext context, Invocation invocation) throws UploadException {
    UploadOptions options = context.getOptions();
    StorageSpace space = context.getSpace();
    Storage storage = context.getStorage();

    FileObject rootFolder = context.getRootFolder();

    IdUtils.FileKey fileKey = IdUtils.parseKey(options.getFolder());
    FileDetail fileDetail = fileKey.getFile();
    String storageId = fileDetail.getStorageConfig().getId();
    FileObject fileObject = fileKey.getFile().toFileObject(space);

    if (!fileObject.getPath().startsWith(rootFolder.getPath())) {
      throw new UploadException("文件夹位置错误");
    }

    if (!fileKey.getFile().getIsDirectory()) {
      throw new UploadException("上传的位置不是文件夹");
    }

    context.setFolder(fileObject);
    context.setStorage(storageResolver.resolve(storageId));

    return invocation.invoke();
  }
}
