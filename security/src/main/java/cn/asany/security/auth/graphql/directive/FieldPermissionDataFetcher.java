package cn.asany.security.auth.graphql.directive;

import cn.asany.security.auth.error.UnauthorizedException;
import graphql.schema.*;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import java.util.Map;
import org.jfantasy.framework.security.authentication.Authentication;
import org.jfantasy.framework.security.authentication.NotAuthenticatedException;
import org.jfantasy.graphql.context.AuthorizationGraphQLServletContext;

/**
 * 权限验证
 *
 * @author limaofeng
 */
public class FieldPermissionDataFetcher implements DataFetcher<Object> {

  private final DataFetcher<?> dataFetcher;

  private final AuthInfo authInfo;

  public FieldPermissionDataFetcher(
      SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> env, AuthInfo authInfo) {
    GraphQLFieldDefinition field = env.getElement();
    GraphQLFieldsContainer parentType = env.getFieldsContainer();

    this.authInfo = authInfo;
    this.dataFetcher = env.getCodeRegistry().getDataFetcher(parentType, field);
  }

  @Override
  public Object get(DataFetchingEnvironment environment) throws Exception {
    @SuppressWarnings("deprecation")
    AuthorizationGraphQLServletContext context = environment.getContext();
    Authentication authentication = context.getAuthentication();

    if (!authentication.isAuthenticated()) {
      throw new NotAuthenticatedException("You need authenticated");
    }

    Map<String, Object> args = environment.getArguments();
    boolean hasPermission = authInfo.checkUserPermission(authentication, args);

    if (!hasPermission) {
      throw new UnauthorizedException(
          "Access denied. You don't have permission to access this resource.");
    }

    return dataFetcher.get(environment);
  }
}
