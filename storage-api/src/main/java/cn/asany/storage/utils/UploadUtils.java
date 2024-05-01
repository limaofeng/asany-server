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
package cn.asany.storage.utils;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.FileObjectMetadata;
import cn.asany.storage.api.UploadFileObject;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.util.common.ClassUtil;
import net.asany.jfantasy.framework.util.common.file.FileUtil;
import org.apache.catalina.core.ApplicationPart;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传工具类
 *
 * @author limaofeng
 */
@Slf4j
public class UploadUtils {

  public static FileObject partToObject(MultipartFile file) {
    ApplicationPart part = ClassUtil.getValue(file, "part");
    return partToObject(part);
  }

  public static MultipartFile partToMultipartFile(Part part) throws IOException {
    return new MockMultipartFile(
        part.getName(), part.getSubmittedFileName(), part.getContentType(), part.getInputStream());
  }

  public static FileObject fileToObject(File file) {
    String mimeType = FileUtil.getMimeType(file);
    return new UploadFileObject(
        file.getName(),
        file,
        FileObjectMetadata.builder().contentLength(file.length()).contentType(mimeType).build());
  }

  public static FileObject fileToObject(String name, File file) {
    String mimeType = FileUtil.getMimeType(file);
    return new UploadFileObject(
        name,
        file,
        FileObjectMetadata.builder().contentLength(file.length()).contentType(mimeType).build());
  }

  public static FileObject partToObject(Part part) {
    if (!(part instanceof ApplicationPart)) {
      throw new RuntimeException("Part 类型不匹配: " + part.getClass());
    }
    DiskFileItem fileItem = ClassUtil.getFieldValue(part, ApplicationPart.class, "fileItem");
    File tempFile = ClassUtil.getFieldValue(fileItem, DiskFileItem.class, "tempFile");
    return new UploadFileObject(
        part.getSubmittedFileName(),
        tempFile,
        FileObjectMetadata.builder()
            .contentLength(part.getSize())
            .contentType(part.getContentType())
            .build());
  }

  @SneakyThrows
  public static String md5(File file) {
    MessageDigest digest = MessageDigest.getInstance("MD5");
    try (FileInputStream fis = new FileInputStream(file)) {
      byte[] byteArray = new byte[8192]; // 8KB buffer size
      int bytesCount;
      // Read file data and update in message digest
      while ((bytesCount = fis.read(byteArray)) != -1) {
        digest.update(byteArray, 0, bytesCount);
      }
    }
    // Convert the byte array into hex format
    StringBuilder sb = new StringBuilder();
    for (byte b : digest.digest()) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }
}
