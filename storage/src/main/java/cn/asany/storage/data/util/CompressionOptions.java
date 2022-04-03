package cn.asany.storage.data.util;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompressionOptions {
  /** 编码 */
  @Builder.Default private String encoding = "utf-8";
  /** 注释 */
  @Builder.Default private String comment = "";
  /** 相对地址 */
  private String relative;
  /** 不生成文件夹 */
  private boolean noFolder;
}
