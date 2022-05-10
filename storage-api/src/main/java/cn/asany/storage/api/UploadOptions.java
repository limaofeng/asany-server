package cn.asany.storage.api;

import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jfantasy.framework.util.common.StringUtil;

/**
 * 上传选项
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadOptions {
  /** 上传空间 */
  private String space;
  /** 上传目录 */
  private String folder;
  /** 启用插件 */
  @Builder.Default private Set<String> plugins = new HashSet<>();
  /** 文件名策略 */
  private UploadFileNameStrategy nameStrategy;

  private String uploadId;
  private String hash;

  public boolean isMultipartUpload() {
    return StringUtil.isNotBlank(this.uploadId);
  }
}
