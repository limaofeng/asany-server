package cn.asany.storage.api;

import java.io.File;
import java.util.List;

public interface IMultipartUpload {

  String initiate(String remotePath, FileObjectMetadata objectMetadata);

  /**
   * 分片上传
   *
   * @param remotePath 文件路径
   * @param uploadId 上传ID
   * @param partNumber 分片序号
   * @param file 文件
   * @param partSize 分片大小
   * @return 分片 ID
   * @throws UploadException 上传异常
   */
  String uploadPart(String remotePath, String uploadId, int partNumber, File file, long partSize)
      throws UploadException;

  String complete(String remotePath, String uploadId, List<String> partETags)
      throws UploadException;

  void abort(String uploadId);
}
