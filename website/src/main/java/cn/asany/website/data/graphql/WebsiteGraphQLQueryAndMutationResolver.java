package cn.asany.website.data.graphql;

import cn.asany.website.data.convert.WebsiteConverter;
import cn.asany.website.data.domain.Website;
import cn.asany.website.data.graphql.input.WebsiteCreateInput;
import cn.asany.website.data.graphql.input.WebsiteFilter;
import cn.asany.website.data.service.WebsiteService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class WebsiteGraphQLQueryAndMutationResolver
    implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final WebsiteService websiteService;

  private final WebsiteConverter websiteConverter;

  public WebsiteGraphQLQueryAndMutationResolver(
      WebsiteService websiteService, WebsiteConverter websiteConverter) {
    this.websiteService = websiteService;
    this.websiteConverter = websiteConverter;
  }

  public List<Website> websites(WebsiteFilter filter, Sort orderBy) {
    return this.websiteService.websites(filter.build(), orderBy);
  }

  public Website website(Long id) {
    return this.websiteService.get(id);
  }

  public Website createWebsite(WebsiteCreateInput input) {
    return this.websiteService.save(websiteConverter.toWebsite(input));
  }
}
