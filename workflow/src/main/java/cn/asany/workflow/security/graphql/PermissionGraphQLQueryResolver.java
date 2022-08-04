package cn.asany.workflow.security.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import java.util.List;
import cn.asany.pm.security.bean.PermissionScheme;
import cn.asany.pm.security.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng@msn.com @ClassName: PermissionGraphQLQueryResolver @Description: (这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
@Component("issuePermissionGraphQLQueryResolver")
public class PermissionGraphQLQueryResolver implements GraphQLQueryResolver {

  @Autowired private PermissionService service;
  /**
   * @ClassName: PermissionGraphQLQueryResolver @Description: 查询全部权限方案
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public List<PermissionScheme> issuePermissionSchemes() {
    return service.permissionSchemes();
  }

  /**
   * @ClassName: PermissionGraphQLQueryResolver @Description: 查询某个权限方案的列表
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public PermissionScheme issuePermissionScheme(Long id) {
    return service.permissionScheme(id);
  }
}
