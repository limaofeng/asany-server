package cn.asany.workflow.security.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import java.util.List;
import net.whir.hos.issue.security.bean.PermissionScheme;
import net.whir.hos.issue.security.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author penghanying @ClassName: PermissionGraphQLQueryResolver @Description: (这里用一句话描述这个类的作用)
 * @date 2019/5/31
 */
@Component("issuePermissionGraphQLQueryResolver")
public class PermissionGraphQLQueryResolver implements GraphQLQueryResolver {

  @Autowired private PermissionService service;
  /**
   * @ClassName: PermissionGraphQLQueryResolver @Description: 查询全部权限方案
   *
   * @author penghanying
   * @date 2019/5/31
   */
  public List<PermissionScheme> issuePermissionSchemes() {
    return service.permissionSchemes();
  }

  /**
   * @ClassName: PermissionGraphQLQueryResolver @Description: 查询某个权限方案的列表
   *
   * @author penghanying
   * @date 2019/5/31
   */
  public PermissionScheme issuePermissionScheme(Long id) {
    return service.permissionScheme(id);
  }
}
