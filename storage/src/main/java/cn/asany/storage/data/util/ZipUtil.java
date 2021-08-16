package cn.asany.storage.data.util;

import cn.asany.storage.api.FileItemSelector;
import cn.asany.storage.api.FileObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.jfantasy.framework.util.common.StreamUtil;

public class ZipUtil {

  private static final Log LOGGER = LogFactory.getLog(ZipUtil.class);

  /**
   * zip压缩
   *
   * @param outputStream 要压缩到的流
   * @param fileObject 文件对象
   * @param comment 说明信息 @功能描述
   */
  public void compress(OutputStream outputStream, FileObject fileObject, String comment) {
    compress(
        outputStream,
        fileObject,
        new FileItemSelector() {

          @Override
          public boolean traverseDescendents(FileObject fileObject) {
            return true;
          }

          @Override
          public boolean includeFile(FileObject fileObject) {
            return true;
          }
        },
        "gbk",
        comment);
  }

  /**
   * @param outputStream
   * @param fileObject
   * @param encoding 压缩时采用的编码
   * @param comment @功能描述
   */
  public void compress(
      OutputStream outputStream, FileObject fileObject, String encoding, String comment) {
    compress(
        outputStream,
        fileObject,
        new FileItemSelector() {

          @Override
          public boolean traverseDescendents(FileObject fileObject) {
            return true;
          }

          @Override
          public boolean includeFile(FileObject fileObject) {
            return true;
          }
        },
        encoding,
        comment);
  }

  /**
   * @param outputStream
   * @param fileObject
   * @param selector
   * @param encoding
   * @param comment @功能描述
   */
  public void compress(
      OutputStream outputStream,
      FileObject fileObject,
      FileItemSelector selector,
      String encoding,
      String comment) {
    ZipOutputStream out = new ZipOutputStream(outputStream);
    List<FileObject> fileObjects = fileObject.listFiles(selector);
    for (FileObject item : fileObjects) {
      ZipEntry entry =
          new ZipEntry(item.getAbsolutePath().replaceFirst(fileObject.getAbsolutePath(), ""));
      try {
        out.putNextEntry(entry);
        InputStream in = fileObject.getInputStream();
        StreamUtil.copy(in, out);
        StreamUtil.closeQuietly(in);
      } catch (IOException e) {
        LOGGER.error(e.getMessage(), e);
      }
    }
    out.setEncoding(encoding);
    out.setComment(comment);
    StreamUtil.closeQuietly(out);
  }
}
