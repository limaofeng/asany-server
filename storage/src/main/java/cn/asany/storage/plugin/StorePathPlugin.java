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
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.util.common.DateUtil;
import net.asany.jfantasy.framework.util.common.StringUtil;
import net.asany.jfantasy.framework.util.common.file.FileUtil;
import org.springframework.stereotype.Component;

/**
 * 存储路径生成
 *
 * @author limaofeng
 */
@Slf4j
@Component
public class StorePathPlugin implements StoragePlugin {

  public static final String ID = "store-path";

  @Override
  public String id() {
    return ID;
  }

  @Override
  public boolean supports(UploadContext context) {
    return StringUtil.isBlank(context.getStorePath()) && !context.isMultipartUpload();
  }

  @Override
  public FileObject upload(UploadContext context, Invocation invocation) throws UploadException {
    VirtualFileObject folder = (VirtualFileObject) context.getFolder();
    UploadFileObject file = context.getFile();

    String mimeType;
    if (!file.isNoFile()) {
      mimeType = FileUtil.getMimeType(file.getFile());
    } else {
      mimeType = StringUtil.defaultValue(file.getMimeType(), () -> "application/octet-stream");
    }

    String extension = FileUtil.getExtension(mimeType);

    String folderPath =
        folder.getStorePath()
            + DateUtil.format("yyyyMMdd")
            + FileObject.SEPARATOR
            + mimeType
            + FileObject.SEPARATOR;

    String filename = StringUtil.shortUUID() + extension;

    context.setStorePath(folderPath + filename);

    return invocation.invoke();
  }
}
