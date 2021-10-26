package cn.asany.storage.api;

/**
 * 存储插件
 *
 * @author limaofeng
 */
public interface StoragePlugin {

  /**
   * 判断是否启用
   *
   * @param context
   * @return
   */
  boolean supports(UploadContext context);

  /**
   * 上传方法
   *
   * @param context
   * @return FileObject
   */
  FileObject upload(UploadContext context);
}
