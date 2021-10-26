package cn.asany.storage.api;

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
  /** 用户定义的文件名称（非上传文件的名称） */
  private String name;
  /** 上传空间 */
  @Builder.Default private String space = "Default";
  /** 插件 */
  private String plugin;
  /** 文件名策略 */
  private String nameStrategy;

  private String entireFileName;
  private String entireFileDir;
  private String entireFileHash;
  private String partFileHash;
  private Integer total;
  private Integer index;

  public boolean isPart() {
    return StringUtil.isNotBlank(this.entireFileHash);
  }

  public String getPartName() {
    return entireFileName
        + ".part"
        + StringUtil.addZeroLeft(index.toString(), total.toString().length());
  }
}
