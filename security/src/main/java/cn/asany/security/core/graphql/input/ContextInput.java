package cn.asany.security.core.graphql.input;

import lombok.Data;

@Data
public class ContextInput {
  /** 是否解析到人 */
  private Boolean parsing;
  /** 用戶id */
  private Long viewer;

  /** 业务ID （解析业务角色时使用） */
  private String businessId;
}
