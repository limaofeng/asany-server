package cn.asany.cms.article.graphql.resolver;

import cn.asany.cms.article.domain.PageComponent;
import cn.asany.ui.resources.domain.toy.ComponentData;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PageComponentGraphQLResolver implements GraphQLResolver<PageComponent> {

  public String template(PageComponent page) {
    if (!page.getEnabled()) {
      return null;
    }
    return page.getComponent().getTemplate();
  }

  public List<ComponentData> blocks(PageComponent page) {
    if (!page.getEnabled()) {
      return null;
    }
    return page.getComponent().getBlocks();
  }
}
