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
import cn.asany.storage.core.engine.virtual.VirtualFileObject;
import cn.asany.storage.data.domain.FileDetail;
import cn.asany.storage.data.domain.Space;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.utils.UploadUtils;
import java.io.File;
import lombok.SneakyThrows;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.framework.util.common.StringUtil;
import net.asany.jfantasy.framework.util.common.file.FileUtil;
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
