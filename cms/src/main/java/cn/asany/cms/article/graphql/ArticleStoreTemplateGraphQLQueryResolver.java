package cn.asany.cms.article.graphql;

import cn.asany.cms.article.domain.ArticleStoreTemplate;
import cn.asany.cms.article.service.ArticleStoreTemplateService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ArticleStoreTemplateGraphQLQueryResolver implements GraphQLQueryResolver {

  private final ArticleStoreTemplateService storeTemplateService;

  public ArticleStoreTemplateGraphQLQueryResolver(
      ArticleStoreTemplateService storeTemplateService) {
    this.storeTemplateService = storeTemplateService;
  }

  public List<ArticleStoreTemplate> articleStoreTemplates() {
    return this.storeTemplateService.storeTemplates();
  }
}
