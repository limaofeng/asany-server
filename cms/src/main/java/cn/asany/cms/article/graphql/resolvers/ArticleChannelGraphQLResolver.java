package cn.asany.cms.article.graphql.resolvers;

import cn.asany.cms.article.bean.ArticleChannel;
import cn.asany.cms.article.graphql.converters.ArticleChannelConverter;
import cn.asany.cms.article.graphql.types.Starrable;
import cn.asany.cms.article.service.ArticleChannelService;
import cn.asany.cms.permission.bean.Permission;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-08-15 09:53
 */
@Component
public class ArticleChannelGraphQLResolver implements GraphQLResolver<ArticleChannel> {

  @Autowired private ArticleChannelService articleChannelService;

  @Autowired private ArticleChannelConverter articleChannelConverter;

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

  public Permission permissions(ArticleChannel channel) {
    return Permission.builder()
        .resourceType("ArticleChannel")
        .id(String.valueOf(channel.getId()))
        .build();
  }

  private List<Permission> loadGrantPermissions(ArticleChannel channel) {
    //        if (channel.getPermissions() == null) {
    //            channel.setPermissions(securityGrpcInvoke.getPermissions("ArticleChannel",
    // String.valueOf(channel.getId())));
    //        }
    return channel.getPermissions();
  }
}
