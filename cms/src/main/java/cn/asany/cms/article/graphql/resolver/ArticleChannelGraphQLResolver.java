package cn.asany.cms.article.graphql.resolver;

import cn.asany.cms.article.bean.Article;
import cn.asany.cms.article.bean.ArticleChannel;
import cn.asany.cms.article.graphql.input.ArticleFilter;
import cn.asany.cms.article.graphql.type.Starrable;
import cn.asany.cms.article.service.ArticleChannelService;
import cn.asany.cms.article.service.ArticleService;
import cn.asany.cms.permission.bean.Permission;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.*;
import java.util.stream.Collectors;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Component;

/**
 * 文章栏目 解析器
 *
 * @author limaofeng
 * @date 2019-08-15 09:53
 */
@Component
public class ArticleChannelGraphQLResolver implements GraphQLResolver<ArticleChannel> {

  private final ArticleChannelService articleChannelService;
  private final ArticleService articleService;

  public ArticleChannelGraphQLResolver(
      ArticleChannelService articleChannelService, ArticleService articleService) {
    this.articleChannelService = articleChannelService;
    this.articleService = articleService;
  }

  public Starrable starrable(ArticleChannel channel) {
    //        .id(channel.getId().toString() + "/" + startType)
    //                .starType(startType).galaxy(channel.getId().toString())
    //                .securityScopes(() -> {
    //                    List<SecurityScope> securityScopes = new ArrayList<>();
    //                    loadGrantPermissions(channel)
    //                            .stream()
    //                            .forEach(item -> item.getGrants().stream().forEach(grant -> {
    //
    // securityScopes.add(SecurityScope.newInstance(grant.getSecurityType(), grant.getValue()));
    //                            }));
    //                    return securityScopes;
    //                })
    String startType = "article_channel_follow";
    return Starrable.builder().build();
  }

  public String fullName(ArticleChannel channel) {
    List<ArticleChannel> parents = this.parents(channel);
    if (parents.isEmpty()) {
      return channel.getName();
    }
    return parents.stream().map(ArticleChannel::getName).collect(Collectors.joining("."))
        + "."
        + channel.getName();
  }

  public List<ArticleChannel> parents(ArticleChannel channel) {
    List<Long> ids =
        Arrays.stream(StringUtil.tokenizeToStringArray(channel.getPath(), ArticleChannel.SEPARATOR))
            .map(Long::valueOf)
            .filter(item -> !item.equals(channel.getId()))
            .collect(Collectors.toList());

    if (ids.isEmpty()) {
      return Collections.emptyList();
    }

    List<ArticleChannel> parents =
        this.articleChannelService.findAll(PropertyFilter.builder().in("id", ids).build());

    return ids.stream().map(id -> ObjectUtil.find(parents, "id", id)).collect(Collectors.toList());
  }

  public List<Article> articles(
      ArticleChannel channel,
      /* 包含所有后代 */
      Boolean descendant,
      /* 筛选 */
      ArticleFilter filter,
      /* 跳过 */
      int skip,
      /* 游标定位 之后 */
      Long after,
      /* 游标定位 之前 */
      Long before,
      /* 开始几条 */
      int first,
      /* 之前几条 */
      int last,
      /* 排序 */
      OrderBy orderBy) {
    Pager<Article> pager = new Pager<>(first);
    pager.setOrderBy(orderBy);
    pager.setOffset(skip);

    PropertyFilterBuilder builder = PropertyFilter.builder();

    if (descendant) {
      builder.startsWith("channels.path", channel.getPath());
    } else {
      builder.equal("channels.id", channel.getId());
    }

    if (filter != null) {
      builder.and(filter.getBuilder());
    }

    pager = this.articleService.findPager(pager, builder.build());
    return pager.getPageItems();
  }

  public Permission permissions(ArticleChannel channel) {
    return Permission.builder()
        .resourceType("ArticleChannel")
        .id(String.valueOf(channel.getId()))
        .build();
  }
}
