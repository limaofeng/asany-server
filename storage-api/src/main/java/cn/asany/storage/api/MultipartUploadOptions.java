package cn.asany.storage.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultipartUploadOptions {
  /** 上传空间 */
  @Builder.Default private String space = "Default";
  /** 上传目录 */
  private String folder;
  /** 文件名策略 */
  private UploadFileNameStrategy nameStrategy;
  /** Hash 值 */
  private String hash;
  /** 每段大小 */
  private Long chunkSize;
  /** 总的段数 */
  private Integer chunkLength;
}
