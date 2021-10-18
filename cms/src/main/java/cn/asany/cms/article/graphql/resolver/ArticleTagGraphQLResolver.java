package cn.asany.cms.article.graphql.resolver;

import cn.asany.cms.article.bean.ArticleTag;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

/**
 * 文章标签
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-06-26 17:26
 */
@Component
public class ArticleTagGraphQLResolver implements GraphQLResolver<ArticleTag> {

  public String url(ArticleTag tag) {
    return tag.getSlug();
  }
}
