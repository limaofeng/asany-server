package cn.asany.security.core.graphql.types;

import cn.asany.base.common.SecurityType;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-07-26 14:26
 */
@Data
@Builder
public class SecurityScopeObject {
  private String id;
  private SecurityType type;
  private String name;
  private Map<String, Object> filter;
  private Object value;
}
