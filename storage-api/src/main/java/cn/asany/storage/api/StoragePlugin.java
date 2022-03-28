package cn.asany.storage.api;

/**
 * 存储插件
 *
 * @author limaofeng
 */
public interface StoragePlugin {

  String id();

  /**
   * 判断是否启用
   *
   * @param context 上下文
   * @return boolean
   */
  boolean supports(UploadContext context);

  /**
   * 上传方法
   *
   * @param context 上下文
   * @return FileObject
   */
  FileObject upload(UploadContext context, Invocation invocation) throws UploadException;
}
