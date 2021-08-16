package cn.asany.security.core.graphql.models;

import cn.asany.base.common.SecurityType;
import java.io.Serializable;
import lombok.Data;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-06-12 13:59
 */
@Data
public class GrantPermissionInput implements Serializable {
  private SecurityType securityType;
  private String permission;
  private String value;
}
