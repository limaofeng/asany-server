package cn.asany.storage.api;

import java.io.IOException;

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
   * @return
   * @throws IOException
   */
  FileObject upload(FileObject object, UploadOptions options) throws IOException;
}
