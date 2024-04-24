package cn.asany.storage.utils;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.FileObjectMetadata;
import cn.asany.storage.api.UploadFileObject;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.error.IgnoreException;
import net.asany.jfantasy.framework.util.common.ClassUtil;
import net.asany.jfantasy.framework.util.common.StreamUtil;
import net.asany.jfantasy.framework.util.common.file.FileUtil;
import org.apache.catalina.core.ApplicationPart;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.DigestUtils;
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

  public static String md5(File file) {
    InputStream input = null;
    try {
      input = Files.newInputStream(file.toPath());
      return DigestUtils.md5DigestAsHex(input);
    } catch (IOException e) {
      log.error(e.getMessage());
      throw new IgnoreException(e);
    } finally {
      StreamUtil.closeQuietly(input);
    }
  }
}
