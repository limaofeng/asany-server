package cn.asany.cms.article.graphql.resolver;

import cn.asany.cms.article.bean.ArticleChannel;
import cn.asany.cms.article.graphql.type.Starrable;
import cn.asany.cms.article.service.ArticleChannelService;
import cn.asany.cms.permission.bean.Permission;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.*;
import java.util.stream.Collectors;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
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

  public ArticleChannelGraphQLResolver(ArticleChannelService articleChannelService) {
    this.articleChannelService = articleChannelService;
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

  public Permission permissions(ArticleChannel channel) {
    return Permission.builder()
        .resourceType("ArticleChannel")
        .id(String.valueOf(channel.getId()))
        .build();
  }
}
