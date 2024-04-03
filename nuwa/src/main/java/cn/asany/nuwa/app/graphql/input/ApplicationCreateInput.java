package cn.asany.nuwa.app.graphql.input;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建应用的表单
 *
 * @author limaofeng
 */
@Data
@NoArgsConstructor
public class ApplicationCreateInput {
  /** 名称 */
  private String name;
  /** 简介 */
  private String description;
}
