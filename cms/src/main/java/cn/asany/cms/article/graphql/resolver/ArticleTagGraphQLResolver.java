package cn.asany.cms.article.graphql.resolver;

import cn.asany.cms.article.domain.ArticleTag;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

/**
 * 文章标签
 *
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
@Component
public class ArticleTagGraphQLResolver implements GraphQLResolver<ArticleTag> {

  public String url(ArticleTag tag) {
    return tag.getSlug();
  }
}
