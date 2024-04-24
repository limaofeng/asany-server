package cn.asany.security.auth.graphql.directive;

import cn.asany.security.auth.error.UnauthorizedException;
import graphql.schema.*;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import java.util.Map;
import net.asany.jfantasy.framework.security.authentication.Authentication;
import net.asany.jfantasy.framework.security.authentication.NotAuthenticatedException;
import net.asany.jfantasy.graphql.security.context.AuthGraphQLServletContext;

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
    AuthGraphQLServletContext context = environment.getContext();
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

    authInfo.condition(authentication, args);

    return dataFetcher.get(environment);
  }
}
