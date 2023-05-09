package cn.asany.storage.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 文件管理接口
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-9-28 下午12:09:54
 */
public interface Storage {
  String DEFAULT_STORAGE_ID = "default";
  /**
   * ID
   *
   * @return 存储器 ID
   */
  String getId();
  /**
   * 将一个 File 对象写入地址对应的文件中
   *
   * @param remotePath 路径
   * @param file 文件对象
   * @throws IOException 异常
   */
  void writeFile(String remotePath, File file) throws IOException;

  /**
   * 将 InputStream 写到地址对应的文件中
   *
   * @param remotePath 路径
   * @param in 输出流
   *           @throws IOException 异常
   */
  void writeFile(String remotePath, InputStream in) throws IOException;

  /**
   * 返回一个 out 流，用来接收要写入的内容
   *
   * @param remotePath 路径
   * @return {OutputStream}
   * @throws IOException 异常
   */
  OutputStream writeFile(String remotePath) throws IOException;

  /**
   * 通过地址将一个文件写到一个本地地址
   *
   * @param remotePath 路径
   * @param localPath 路径
   *                  @throws IOException 异常
   */
  void readFile(String remotePath, String localPath) throws IOException;

  /**
   * 通过地址将文件写入到 OutputStream 中
   *
   * @param remotePath 路径
   * @param out 输出流
   *            @throws IOException 异常
   */
  void readFile(String remotePath, OutputStream out) throws IOException;

  /**
   * 通过一个地址获取文件对应的 InputStream
   *
   * @param remotePath 路径
   * @return 返回 InputStream 对象
   * @throws IOException 异常
   */
  InputStream readFile(String remotePath) throws IOException;

  /**
   * 通过一个地址获取文件对应的 InputStream
   *
   * @param remotePath 路径 @Param range 范围下载
   * @param range 范围下载
   * @return 返回 InputStream 对象
   * @throws IOException 异常
   */
  default InputStream readFile(String remotePath, long[] range) throws IOException {
    return this.getFileItem(remotePath).getInputStream(range[0], range[1]);
  }

  /**
   * 获取跟目录的文件列表
   *
   * @return {List<FileItem>}
   */
  List<FileObject> listFiles();

  /**
   * 获取指定路径下的文件列表
   *
   * @param remotePath 路径
   * @return {List<FileItem>}
   */
  List<FileObject> listFiles(String remotePath);

  /**
   * 通过 FileItemSelector 接口 筛选文件,当前对象必须为文件夹，此方法有效
   *
   * @param selector FileItemSelector
   * @return {List<FileItem>}
   */
  List<FileObject> listFiles(FileItemSelector selector);

  /**
   * 通过 FileItemSelector 接口 筛选文件,当前对象必须为文件夹，此方法有效
   *
   * @param remotePath 路径
   * @param selector FileItemSelector
   * @return {List<FileItem>}
   */
  List<FileObject> listFiles(String remotePath, FileItemSelector selector);

  /**
   * 通过 FileItemFilter 接口 筛选文件,当前对象必须为文件夹，此方法有效
   *
   * @param filter FileItemFilter
   * @return {List<FileItem>}
   */
  List<FileObject> listFiles(FileItemFilter filter);

  /**
   * 通过 FileItemFilter 接口 筛选文件,当前对象必须为文件夹，此方法有效
   *
   * @param remotePath 路径
   * @param filter FileItemFilter
   * @return {List<FileItem>}
   */
  List<FileObject> listFiles(String remotePath, FileItemFilter filter);

  /**
   * 获取目录对应的 FileItem 对象
   *
   * @param remotePath 地址
   * @return {FileItem}
   */
  FileObject getFileItem(String remotePath);

  /**
   * 删除地址对应的文件
   *
   * @param remotePath 地址
   *                   <p>如果是文件夹，会删除文件夹下的所有文件</p>
   *                   <p>如果是文件，会删除文件</p>
   *                   <p>如果是不存在的文件，会抛出异常</p>
   *                   <p>如果是不存在的文件夹，会抛出异常</p>
   * @throws IOException IOException
   */
  void removeFile(String remotePath) throws IOException;

  /**
   * 是否支持 分片上传
   *
   * @return boolean
   */
  default boolean isMultipartUploadSupported() {
    return this.multipartUpload() != null;
  }

  /**
   * 分片上传 API
   *
   * @return MultipartUpload
   */
  default IMultipartUpload multipartUpload() {
    return null;
  }
}
