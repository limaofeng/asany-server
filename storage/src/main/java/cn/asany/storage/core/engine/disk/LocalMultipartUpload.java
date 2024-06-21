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
package cn.asany.storage.core.engine.disk;

import cn.asany.storage.api.FileObjectMetadata;
import cn.asany.storage.api.IMultipartUpload;
import cn.asany.storage.api.UploadException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lombok.SneakyThrows;
import net.asany.jfantasy.framework.util.common.StreamUtil;
import net.asany.jfantasy.framework.util.common.StringUtil;
import net.asany.jfantasy.framework.util.common.file.FileUtil;

public class LocalMultipartUpload implements IMultipartUpload {

  private final Path baseDir;
  private final Path partBaseDir;

  @SneakyThrows
  public LocalMultipartUpload(String baseDir) {
    this.baseDir = Path.of(baseDir);
    this.partBaseDir = this.baseDir.resolve(".parts");
    if (Files.notExists(this.partBaseDir)) {
      Files.createDirectory(this.partBaseDir);
    }
  }

  @Override
  public String initiate(String remotePath, FileObjectMetadata objectMetadata) {
    return StringUtil.uuid();
  }

  @Override
  public String uploadPart(
      String remotePath, String uploadId, int partNumber, File file, long partSize)
      throws UploadException {
    String etag = StringUtil.uuid();
    try {
      Path filePath =
          FileUtil.createFile(this.partBaseDir.resolve(uploadId + "/" + etag + ".part"));
      StreamUtil.copyThenClose(new FileInputStream(file), Files.newOutputStream(filePath));
      return etag;
    } catch (IOException e) {
      throw new UploadException(e.getMessage());
    }
  }

  @Override
  public String complete(String remotePath, String uploadId, List<String> partETags)
      throws UploadException {
    try {
      Path filePath =
          FileUtil.createFile(
              baseDir.resolve(remotePath.startsWith("/") ? remotePath.substring(1) : remotePath));
      OutputStream output = Files.newOutputStream(filePath);
      for (String etag : partETags) {
        InputStream input =
            Files.newInputStream(this.partBaseDir.resolve(uploadId + "/" + etag + ".part"));
        StreamUtil.copy(input, output);
        StreamUtil.closeQuietly(input);
      }
      StreamUtil.closeQuietly(output);
      return remotePath;
    } catch (IOException e) {
      throw new UploadException(e.getMessage());
    }
  }

  @Override
  public void abort(String uploadId) {}
}
