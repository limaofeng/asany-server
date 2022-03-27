package cn.asany.storage.api;

/**
 * 上传服务
 *
 * @author limaofeng
 */
public interface UploadService {

  /**
   * 上传接口
   *
   * @param object 上传文件对象
   * @param options 上传选项
   * @return FileObject
   * @throws UploadException 异常
   */
  FileObject upload(FileObject object, UploadOptions options) throws UploadException;

  /**
   * 初始化分片上传
   *
   * @param name 文件名
   * @param options 选项
   * @return Upload Id
   * @throws UploadException 异常
   */
  String initiateMultipartUpload(String name, MultipartUploadOptions options)
      throws UploadException;
}
