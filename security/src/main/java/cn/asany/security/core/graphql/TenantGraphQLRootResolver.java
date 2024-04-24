package cn.asany.security.core.graphql;

import cn.asany.security.core.graphql.input.TenantWhereInput;
import cn.asany.security.core.graphql.types.TenantConnection;
import cn.asany.security.core.service.TenantService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import net.asany.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class TenantGraphQLRootResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

  public final TenantService tenantService;

  public TenantGraphQLRootResolver(TenantService tenantService) {
    this.tenantService = tenantService;
  }

  public TenantConnection tenants(TenantWhereInput where, int page, int pageSize, Sort orderBy) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    return Kit.connection(
        tenantService.findPage(pageable, where.toFilter()), TenantConnection.class);
  }
}
