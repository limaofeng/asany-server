package cn.asany.cms.article.graphql.resolver;

import cn.asany.cms.article.bean.ArticleTag;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-06-26 17:26
 */
@Component
public class ArticleTagGraphQLResolver implements GraphQLResolver<ArticleTag> {

  public String url(ArticleTag tag) {
    return tag.getSlug();
  }
}
