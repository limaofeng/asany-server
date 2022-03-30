package cn.asany.storage.api;

public enum UploadFileNameStrategy {
  /** 每次上传，生成新的 UUID 作为文件名, 辨识度低 */
  UUID,
  /** 使用上传时的原始文件名存储，但文件名在同一文件夹不能重复, 如果重复自动重命名 */
  ORIGINAL
}
