package cn.asany.shanhai.gateway.graphql.resolvers;

import cn.asany.shanhai.gateway.domain.ModelGroupItem;
import cn.asany.shanhai.gateway.graphql.enums.ModelGroupItemResourceType;
import graphql.kickstart.tools.GraphQLResolver;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.ognl.OgnlUtil;
import org.springframework.stereotype.Component;

/**
 * ModelGroupItemResource GraphQLResolver
 *
 * @author limaofeng
 */
@Component
public class ModelGroupItemResourceGraphQLResolver implements GraphQLResolver<ModelGroupItem> {

  private static final OgnlUtil OGNL_UTIL = OgnlUtil.getInstance();

  public String name(ModelGroupItem resource) {
    return StringUtil.defaultValue(
        (Object) OGNL_UTIL.getValue("resource.name", resource),
        (String) OGNL_UTIL.getValue("resource.code", resource));
  }

  public ModelGroupItemResourceType type(ModelGroupItem resource) {
    if ("ENDPOINT".equals(resource.getResourceType())) {
      String type = OGNL_UTIL.getValue("resource.model.code", resource);
      return ModelGroupItemResourceType.valueOf(type);
    }
    return ModelGroupItemResourceType.Model;
  }
}
