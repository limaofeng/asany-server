package cn.asany.storage.data.util;

import cn.asany.storage.api.FileObject;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompressionOptions {
  /** 编码 */
  @Builder.Default private String encoding = "utf-8";
  /** 注释 */
  @Builder.Default private String comment = "";
  /** 地址转换 */
  private PathForward forward;

  public static interface PathForward {
    String exec(FileObject file);
  }
}
