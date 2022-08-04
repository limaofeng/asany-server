package cn.asany.security.core.graphql.models;

import lombok.Data;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
@Data
public class BusinessScopeInput {

  private String name;

  private String code;
  private Boolean enabled;
}
