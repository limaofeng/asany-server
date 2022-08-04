package cn.asany.cms.article.graphql.resolver;

import cn.asany.cms.article.domain.Article;
import cn.asany.cms.article.domain.ArticleBody;
import cn.asany.cms.article.domain.ArticleCategory;
import cn.asany.cms.article.graphql.enums.ArticleStarType;
import cn.asany.cms.article.graphql.input.CommentFilter;
import cn.asany.cms.article.graphql.type.CommentConnection;
import cn.asany.cms.article.graphql.type.Starrable;
import cn.asany.security.core.domain.Permission;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * 文章
 *
 * @author limaofeng
 * @date 2022/7/28 9:12 9:12
 */
@Component
public class ArticleGraphQLResolver implements GraphQLResolver<Article> {

  private final ArticleCategoryGraphQLResolver articleCategoryGraphQLResolver;

  public ArticleGraphQLResolver(ArticleCategoryGraphQLResolver articleCategoryGraphQLResolver) {
    this.articleCategoryGraphQLResolver = articleCategoryGraphQLResolver;
  }

  public List<ArticleCategory> categories(Article article) {
    if (article.getCategory() == null) {
      return new ArrayList<>();
    }
    ArticleCategory category = article.getCategory();
    List<ArticleCategory> categories = articleCategoryGraphQLResolver.parents(category);
    categories.add(category);
    return categories;
  }

  public String bodyHtml(Article article) {
    return "";
  }

  public ArticleBody body(Article article) {
    return article.getBody();
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

  public CommentConnection comments(
      Article article, CommentFilter filter, int page, int pageSize, Sort orderBy) {
    CommentConnection comments = new CommentConnection();
    //    if (article.getCategory() == ArticleCategory.news) {
    //      comments =
    //          resolver.comments(
    //              CommentTargetType.news, article.getId().toString(), filter, page, pageSize,
    // orderBy);
    //    } else if (article.getCategory() == ArticleCategory.circle) {
    //      comments =
    //          resolver.comments(
    //              CommentTargetType.circle,
    //              article.getId().toString(),
    //              filter,
    //              page,
    //              pageSize,
    //              orderBy);
    //    } else if (article.getCategory() == ArticleCategory.blog) {
    //      comments =
    //          resolver.comments(
    //              CommentTargetType.blog, article.getId().toString(), filter, page, pageSize,
    // orderBy);
    //    }
    return comments;
  }

  public Permission permissions(Article article) {
    return null; // Permission.builder().resourceType("Article").id(String.valueOf(article.getId())).build();
  }

  private List<Permission> loadGrantPermissions(Article article) {
    //        if (article.getPermissions() == null) {
    //            article.setPermissions(securityGrpcInvoke.getPermissions("Article",
    // String.valueOf(article.getId())));
    //        }
    return new ArrayList<>(); // article.getPermissions();
  }
}
