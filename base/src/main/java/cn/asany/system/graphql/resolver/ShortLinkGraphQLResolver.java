package cn.asany.system.graphql.resolver;

import cn.asany.system.domain.ShortLink;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

@Component
public class ShortLinkGraphQLResolver implements GraphQLResolver<ShortLink> {}
