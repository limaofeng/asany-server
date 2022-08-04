package cn.asany.security.core.graphql.resolver;

import cn.asany.security.core.graphql.types.SecurityScopeObject;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
@Component
public class SecurityScopeGraphQLResolver implements GraphQLResolver<SecurityScopeObject> {}
