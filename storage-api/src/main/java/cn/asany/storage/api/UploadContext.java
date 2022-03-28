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
  private UploadService uploadService;

  /** 上传文件 */
  @Getter private UploadFileObject file;
  /** 存储空间 */
  @Getter private StorageSpace space;
  /** 上传参数 */
  @Getter private UploadOptions options;
  /** 根目录 */
  @Getter private String rootFolder;
  /** 上传到的 Storage 目录 */
  @Getter @Setter private String folder;
  /** 上传到的 Storage 名称 */
  @Getter @Setter private String filename;
  /** 存储器 */
  @Getter private Storage storage;

  public FileObject upload(File file, String space) throws IOException {
    return this.uploadService.upload(
        new UploadFileObject(file), UploadOptions.builder().space(space).build());
  }
}
