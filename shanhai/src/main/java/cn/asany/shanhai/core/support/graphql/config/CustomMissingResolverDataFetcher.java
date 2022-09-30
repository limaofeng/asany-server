package cn.asany.shanhai.core.support.graphql.config;

import graphql.language.Field;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLObjectType;
import java.util.HashMap;
import java.util.Map;
import org.jfantasy.framework.util.ognl.OgnlUtil;

public class CustomMissingResolverDataFetcher implements DataFetcher<Object> {

  public CustomMissingResolverDataFetcher() {
    System.out.println("CustomMissingResolverDataFetcher");
  }

  @Override
  public Object get(DataFetchingEnvironment environment) throws Exception {
    GraphQLObjectType graphQLType = (GraphQLObjectType) environment.getParentType();
    Field field = environment.getField();
    Object source = environment.getSource();
    if ("Query".equals(graphQLType.getName())) {
      Map<String, String> data = new HashMap<>();
      data.put("myField", "11213");
      return data;
    }
    if ("MyType".equals(graphQLType.getName())) {
      if ("myField".equals(field.getName())) {
        return OgnlUtil.getInstance().getValue(field.getName(), source);
      }
    }

    return null;
  }
}
