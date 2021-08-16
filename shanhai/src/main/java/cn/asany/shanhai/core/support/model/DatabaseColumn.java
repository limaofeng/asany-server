package cn.asany.shanhai.core.support.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DatabaseColumn {
  /** 数据库列名 */
  private String name;
  /** 列长度 */
  @Builder.Default private Integer length = 255;
  /** 精度 */
  @Builder.Default private Integer precision = 0;
  /** 缩放策略 */
  @Builder.Default private Integer scale = 0;
  /** 可以为空 */
  @Builder.Default private Boolean nullable = true;
  /** 唯一 */
  @Builder.Default private Boolean unique = false;
  /** 可插入的 */
  @Builder.Default private Boolean insertable = true;
  /** 可修改的 */
  @Builder.Default private Boolean updatable = true;
}
