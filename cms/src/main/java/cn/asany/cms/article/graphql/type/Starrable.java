package cn.asany.cms.article.graphql.type;

import cn.asany.cms.permission.service.SecurityScope;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-08-14 11:34
 */
@Data
@Builder
public class Starrable {
  private String id;
  private String galaxy;
  private String starType;
  private GetSecurityScopes securityScopes;

  public List<SecurityScope> getSecurityScopes() {
    if (securityScopes == null) {
      return new ArrayList<>();
    }
    return securityScopes.apply();
  }

  public static interface GetSecurityScopes {
    List<SecurityScope> apply();
  }
}
