package cn.asany.storage.data.graphql.input;

import lombok.Data;

@Data
public class AcceptFolder {
  /** 是否包含子文件夹 */
  private Boolean subfolders = Boolean.FALSE;
  /** 文件夹 */
  private String id;
}
