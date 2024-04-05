package cn.asany.system.graphql;

import cn.asany.base.common.BatchPayload;
import cn.asany.system.convert.ShortLinkConverter;
import cn.asany.system.domain.ShortLink;
import cn.asany.system.graphql.input.ShortLinkCreateInput;
import cn.asany.system.graphql.input.ShortLinkWhereInput;
import cn.asany.system.graphql.type.ShortLinkConnection;
import cn.asany.system.graphql.type.ShortLinkIdType;
import cn.asany.system.service.ShortLinkService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class ShortLinkGraphQLQueryAndMutationResolver
    implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final ShortLinkService shortLinkService;
  private final ShortLinkConverter shortLinkConverter;

  public ShortLinkGraphQLQueryAndMutationResolver(
      ShortLinkService shortLinkService, ShortLinkConverter shortLinkConverter) {
    this.shortLinkService = shortLinkService;
    this.shortLinkConverter = shortLinkConverter;
  }

  public List<ShortLink> generateShortLinks(List<ShortLinkCreateInput> links) {
    if (links.isEmpty()) {
      throw new IllegalArgumentException("count must be greater than 0");
    }
    return shortLinkService.generateShortLinks(shortLinkConverter.toShortLinks(links));
  }

  public Optional<ShortLink> shortLink(String id, ShortLinkIdType type) {
    if (type == ShortLinkIdType.ID) {
      return this.shortLinkService.findById(Long.valueOf(id));
    }
    return this.shortLinkService.findByCode(id);
  }

  public ShortLinkConnection shortLinksConnection(
      ShortLinkWhereInput where, int currentPage, int pageSize, Sort orderBy) {
    PropertyFilter filter = where.toFilter();
    Page<ShortLink> page =
        this.shortLinkService.findPage(PageRequest.of(currentPage - 1, pageSize, orderBy), filter);
    return Kit.connection(page, ShortLinkConnection.class);
  }

  public Optional<ShortLink> deleteShortLink(Long id) {
    return this.shortLinkService.delete(id);
  }

  public BatchPayload deleteManyShortLinks(ShortLinkWhereInput where) {
    return BatchPayload.of(this.shortLinkService.deleteMany(where.toFilter()));
  }
}
