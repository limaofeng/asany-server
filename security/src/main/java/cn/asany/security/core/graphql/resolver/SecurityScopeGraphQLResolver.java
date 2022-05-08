package cn.asany.security.core.graphql.resolver;

import cn.asany.security.core.graphql.types.SecurityScopeObject;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-07-26 14:43
 */
@Component
public class SecurityScopeGraphQLResolver implements GraphQLResolver<SecurityScopeObject> {}
