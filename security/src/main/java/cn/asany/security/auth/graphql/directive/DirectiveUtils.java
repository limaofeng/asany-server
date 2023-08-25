package cn.asany.security.auth.graphql.directive;

import graphql.schema.*;
import org.jfantasy.framework.error.ValidationException;

public class DirectiveUtils {

  public static <T> T getArgumentValue(GraphQLDirective directive, String name) {
    GraphQLArgument descriptionArgument = directive.getArgument(name);
    return descriptionArgument == null ? null : descriptionArgument.toAppliedArgument().getValue();
  }

  public static <T> T getArgumentValue(GraphQLDirective directive, String name, T defaultValue) {
    GraphQLArgument descriptionArgument = directive.getArgument(name);
    return descriptionArgument == null
        ? defaultValue
        : descriptionArgument.toAppliedArgument().getValue();
  }

  public static String getResourceName(GraphQLFieldDefinition field) {
    GraphQLTypeReference type = getReturnType(field.getType());
    return type.getName();
  }

  public static GraphQLTypeReference getReturnType(GraphQLType type) {
    if (type instanceof GraphQLNonNull) {
      return getReturnType(((GraphQLNonNull) type).getWrappedType());
    } else if (type instanceof GraphQLList) {
      return getReturnType(((GraphQLList) type).getWrappedType());
    } else if (type instanceof GraphQLTypeReference) {
      return (GraphQLTypeReference) type;
    } else {
      throw new ValidationException(" 不支持的 GraphQLType = " + type.toString());
    }
  }
}
