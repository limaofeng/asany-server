package cn.asany.storage.data.util;

import cn.asany.storage.api.FileItemSelector;
import cn.asany.storage.api.FileObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.util.common.StreamUtil;
import net.asany.jfantasy.framework.util.web.WebUtil;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

@Slf4j
public class ZipUtil {

  /**
   * 压缩文件
   *
   * @param files 文件列表
   * @param outputStream 输出流
   * @param options 压缩选项
   */
  public static void compress(
      List<FileObject> files, OutputStream outputStream, CompressionOptions options) {
    ZipOutputStream out = new ZipOutputStream(outputStream);
    Set<String> names = new HashSet<>();
    for (FileObject file : files) {
      if (file.isDirectory()) {
        List<FileObject> subfiles = file.listFiles(new FileItemSelector() {});
        for (FileObject item : subfiles) {
          if (item.isDirectory()) {
            continue;
          }
          appendFile(item, out, names, options);
        }
      } else {
        appendFile(file, out, names, options);
      }
    }
    out.setEncoding(options.getEncoding());
    out.setComment(options.getComment());
    StreamUtil.closeQuietly(out);
  }

  private static String convertName(
      FileObject object, Set<String> names, CompressionOptions options) {
    String name = object.getPath();

    CompressionOptions.PathForward pathForward = options.getForward();
    if (pathForward != null) {
      name = pathForward.exec(object);
    }

    int i = 0;
    while (names.contains(name)) {
      String extension = WebUtil.getExtension(name);
      name =
          name.substring(0, name.length() - extension.length())
              + "-"
              + (object.lastModified().getTime() + i++)
              + extension;
    }

    names.add(name);
    return name;
  }

  private static void appendFile(
      FileObject object, ZipOutputStream out, Set<String> names, CompressionOptions options) {
    ZipEntry entry = new ZipEntry(convertName(object, names, options));
    try {
      out.putNextEntry(entry);
      InputStream in = object.getInputStream();
      StreamUtil.copy(in, out);
      StreamUtil.closeQuietly(in);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
  }

  /**
   * 压缩文件
   *
   * @param fileObject 根目录
   * @param selector 选择器
   * @param outputStream 输出流
   */
  public static void compress(
      FileObject fileObject,
      FileItemSelector selector,
      OutputStream outputStream,
      CompressionOptions options) {
    ZipOutputStream out = new ZipOutputStream(outputStream);
    List<FileObject> fileObjects = fileObject.listFiles(selector);
    for (FileObject item : fileObjects) {
      ZipEntry entry = new ZipEntry(item.getPath().replaceFirst(fileObject.getPath(), ""));
      try {
        out.putNextEntry(entry);
        InputStream in = item.getInputStream();
        StreamUtil.copy(in, out);
        StreamUtil.closeQuietly(in);
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }
    }
    out.setEncoding(options.getEncoding());
    out.setComment(options.getComment());
    StreamUtil.closeQuietly(out);
  }
}
