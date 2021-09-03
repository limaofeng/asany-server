package cn.asany.cms.article.graphql.resolvers;

import cn.asany.cms.article.bean.Article;
import cn.asany.cms.article.bean.Content;
import cn.asany.cms.article.bean.enums.ArticleCategory;
import cn.asany.cms.article.bean.enums.ArticleType;
import cn.asany.cms.article.bean.enums.CommentTargetType;
import cn.asany.cms.article.graphql.CommentGraphQLQueryResolver;
import cn.asany.cms.article.graphql.converters.ArticleChannelConverter;
import cn.asany.cms.article.graphql.enums.ArticleStarType;
import cn.asany.cms.article.graphql.inputs.CommentFilter;
import cn.asany.cms.article.graphql.inputs.ContentFormat;
import cn.asany.cms.article.graphql.type.IContent;
import cn.asany.cms.article.graphql.types.ArticleChannel;
import cn.asany.cms.article.graphql.types.CommentConnection;
import cn.asany.cms.article.graphql.types.Starrable;
import cn.asany.cms.article.util.ClassConverts;
import cn.asany.cms.permission.bean.Permission;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.List;
import java.util.stream.Collectors;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.error.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-06-26 17:26
 */
@Component
public class ArticleGraphQLResolver implements GraphQLResolver<Article> {
  @Autowired private CommentGraphQLQueryResolver resolver;
  @Autowired private ArticleChannelConverter articleChannelConverter;

  public IContent content(Article article, ContentFormat format) {
    if (article.getContent() == null
        || article.getContent().getText() == null
        || article.getContent().getType() == null) {
      return null;
    }
    Content content = article.getContent();
    switch (content.getType()) {
      case file:
        IContent iContent = ClassConverts.toContent(article.getType(), content.getType(), content);
        return iContent;
      case json:
        if (article.getType() == ArticleType.picture) {
          // 图片类型
          return ClassConverts.toContent(article.getType(), content.getType(), content);
        }
      case html:
        return ClassConverts.toContent(article.getType(), content.getType(), content);
      case link:
        return ClassConverts.toContent(article.getType(), content.getType(), content);
      case markdown:
        throw new ValidationException("暂不支持 markdown 格式");
    }
    return null;
  }

  public Starrable starrable(final Article article, ArticleStarType starType) {
    //        .id(article.getId().toString() + "/" + starType.getValue())
    //                .galaxy(article.getId().toString())
    //                .starType(starType.getValue())
    //                .securityScopes(() -> {
    //                    List<SecurityScope> securityScopes = new ArrayList<>();
    //                    loadGrantPermissions(article)
    //                            .stream()
    //                            .filter(item -> item.getId().equals(starType.getPermissionKey()))
    //                            .forEach(item -> item.getGrants().stream().forEach(grant -> {
    //
    // securityScopes.add(SecurityScope.newInstance(grant.getSecurityType(), grant.getValue()));
    //                            }));
    //                    return securityScopes;
    //                })
    return Starrable.builder().build();
  }

  //    public Employee author(Article article){
  //        if (StringUtil.isEmpty(article.getAuthor()) || "unknown".equals(article.getAuthor())) {
  //            return null;
  //        }
  //        Employee employee = new Employee();
  //        try {
  //            employee.setId(Long.valueOf(article.getAuthor()));
  //        }catch (Exception e){
  //            e.printStackTrace();
  //        }
  //        return employee;
  //    }

  //    public Employee publisher(Article article) {
  //        if (StringUtil.isEmpty(article.getCreator()) || "unknown".equals(article.getCreator()))
  // {
  //            return null;
  //        }
  //        Employee employee = new Employee();
  //        try {
  //            employee.setId(Long.valueOf(article.getCreator()));
  //        }catch (Exception e){
  //            e.printStackTrace();
  //        }
  //        return employee;
  //    }

  //    public Employee modifier(Article article){
  //        if (StringUtil.isBlank(article.getUpdator()) || "unknown".equals(article.getUpdator()))
  // {
  //            return null;
  //        }
  //        return organizationGrpcInvoke.employee(Long.valueOf(article.getUpdator()));
  //    }

  public List<ArticleChannel> channels(Article article) {
    return article.getChannels().stream()
        .map(item -> articleChannelConverter.toChannel(item))
        .collect(Collectors.toList());
  }

  public CommentConnection comments(
      Article article, CommentFilter filter, int page, int pageSize, OrderBy orderBy) {
    CommentConnection comments = new CommentConnection();
    if (article.getCategory() == ArticleCategory.information) {
      comments =
          resolver.comments(
              CommentTargetType.article,
              article.getId().toString(),
              filter,
              page,
              pageSize,
              orderBy);
    } else if (article.getCategory() == ArticleCategory.section) {
      comments =
          resolver.comments(
              CommentTargetType.section,
              article.getId().toString(),
              filter,
              page,
              pageSize,
              orderBy);
    } else if (article.getCategory() == ArticleCategory.circle) {
      comments =
          resolver.comments(
              CommentTargetType.circle,
              article.getId().toString(),
              filter,
              page,
              pageSize,
              orderBy);
    } else if (article.getCategory() == ArticleCategory.blog) {
      comments =
          resolver.comments(
              CommentTargetType.blog, article.getId().toString(), filter, page, pageSize, orderBy);
    }
    return comments;
  }

  public Permission permissions(Article article) {
    return Permission.builder().resourceType("Article").id(String.valueOf(article.getId())).build();
  }

  private List<Permission> loadGrantPermissions(Article article) {
    //        if (article.getPermissions() == null) {
    //            article.setPermissions(securityGrpcInvoke.getPermissions("Article",
    // String.valueOf(article.getId())));
    //        }
    return article.getPermissions();
  }
}
