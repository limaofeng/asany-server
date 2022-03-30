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
   * @param options 选项
   * @return Upload Id
   * @throws UploadException 异常
   */
  String initiateMultipartUpload(MultipartUploadOptions options) throws UploadException;

  /**
   * 完成分片上传
   *
   * @param uploadId 上传ID
   * @param name 文件名
   * @param folder 上传到的文件夹
   * @return FileObject
   */
  FileObject completeMultipartUpload(String uploadId, String name, String folder)
      throws UploadException;
}
