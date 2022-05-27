package cn.asany.cms.article.graphql.resolver;

import cn.asany.cms.article.domain.Banner;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

/** Banner GraphQL Resolver */
@Component
public class BannerGraphQLResolver implements GraphQLResolver<Banner> {}
