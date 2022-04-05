package cn.asany.storage.api;

import java.io.File;
import java.io.IOException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 上传上下文
 *
 * @author limaofeng
 */
@Builder
public class UploadContext {
  /** 上传服务 */
  private UploadService uploadService;
  /** 上传文件 */
  @Getter private UploadFileObject file;
  /** 存储空间 */
  @Getter private StorageSpace space;
  /** 上传参数 */
  @Getter private UploadOptions options;
  /** 根目录 (最好保证虚拟目录与存储目录一直) */
  @Getter private FileObject rootFolder;
  /** 上传到的虚拟目录 */
  @Getter @Setter private FileObject folder;
  /** 上传到的完整路径 */
  @Getter @Setter private String storePath;
  /** 保存到虚拟目录中的文件名 */
  @Getter @Setter private String filename;
  /** 存储器 */
  @Getter @Setter private Storage storage;

  public FileObject upload(File file, String space) throws IOException {
    return this.uploadService.upload(
        new UploadFileObject(file), UploadOptions.builder().space(space).build());
  }

  public boolean isMultipartUpload() {
    return this.options.isMultipartUpload();
  }
}
