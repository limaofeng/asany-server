package cn.asany.shanhai.gateway.graphql.resolvers;

import cn.asany.shanhai.gateway.bean.ModelGroupItem;
import cn.asany.shanhai.gateway.graphql.enums.ModelGroupItemResourceType;
import graphql.kickstart.tools.GraphQLResolver;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.ognl.OgnlUtil;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng
 */
@Component
public class ModelGroupItemResourceGraphQLResolver implements GraphQLResolver<ModelGroupItem> {

    private static OgnlUtil ognlUtil = OgnlUtil.getInstance();

    public String name(ModelGroupItem resource) {
        return StringUtil.defaultValue(ognlUtil.getValue("resource.name", resource), ognlUtil.getValue("resource.code", resource));
    }

    public ModelGroupItemResourceType type(ModelGroupItem resource) {
        if ("ENDPOINT".equals(resource.getResourceType())) {
            String type = ognlUtil.getValue("resource.model.code", resource);
            return ModelGroupItemResourceType.valueOf(type);
        }
        return ModelGroupItemResourceType.Model;
    }

}
