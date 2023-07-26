package cn.asany.security.auth.graphql.directive;

import cn.asany.security.auth.service.AuthInfoService;
import cn.asany.security.core.domain.enums.AccessLevel;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLDirective;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLFieldsContainer;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import java.util.Collections;
import java.util.List;

/**
 * 权限信息
 *
 * @author limaofeng
 */
public class AuthDirective implements SchemaDirectiveWiring {

  private final AuthInfoService authInfoService;

  public AuthDirective(AuthInfoService authInfoService) {
    this.authInfoService = authInfoService;
  }

  @Override
  public GraphQLFieldDefinition onField(
      SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> env) {
    GraphQLFieldDefinition field = env.getElement();
    GraphQLFieldsContainer parentType = env.getFieldsContainer();
    GraphQLDirective directive = env.getDirective();

    String name = directive.getArgument("name").toAppliedArgument().getValue();
    GraphQLArgument descriptionArgument = directive.getArgument("description");
    AccessLevel accessLevel =
        AccessLevel.valueOf(directive.getArgument("accessLevel").toAppliedArgument().getValue());
    String description =
        descriptionArgument == null
            ? field.getDescription()
            : descriptionArgument.toAppliedArgument().getValue();
    GraphQLArgument resourceTypeListArgument = directive.getArgument("resourceTypes");
    List<String> resourceTypes =
        resourceTypeListArgument == null
            ? Collections.emptyList()
            : resourceTypeListArgument.toAppliedArgument().getValue();

    AuthInfo authInfo = authInfoService.save(name, description, accessLevel, resourceTypes);

    env.getCodeRegistry()
        .dataFetcher(parentType, field, new FieldPermissionDataFetcher(env, authInfo));
    return field;
  }
}
