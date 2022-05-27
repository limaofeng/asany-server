package cn.asany.cms.article.graphql.resolver;

import cn.asany.cms.article.domain.ArticleFeature;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

/**
 * 文章特征
 *
 * @author limaofeng
 */
@Component
public class ArticleFeatureGraphQLResolver implements GraphQLResolver<ArticleFeature> {}
