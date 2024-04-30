/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.cms.article.graphql.resolver;

import cn.asany.cms.article.domain.Article;
import cn.asany.cms.article.domain.ArticleCategory;
import cn.asany.cms.article.domain.ArticleContent;
import cn.asany.cms.article.domain.ArticleInteraction;
import cn.asany.cms.article.graphql.enums.ArticleStarType;
import cn.asany.cms.article.graphql.input.CommentWhereInput;
import cn.asany.cms.article.graphql.type.CommentConnection;
import cn.asany.cms.article.graphql.type.Starrable;
import cn.asany.cms.content.service.ArticleContentService;
import cn.asany.security.core.domain.PermissionStatement;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

  private final ArticleContentService articleContentService;
  private final ArticleCategoryGraphQLResolver articleCategoryGraphQLResolver;

  public ArticleGraphQLResolver(
      ArticleCategoryGraphQLResolver articleCategoryGraphQLResolver,
      ArticleContentService articleContentService) {
    this.articleCategoryGraphQLResolver = articleCategoryGraphQLResolver;
    this.articleContentService = articleContentService;
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

  public Optional<ArticleContent> content(Article article) {
    return articleContentService.findById(article.getContentType(), article.getContentId());
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
      Article article, CommentWhereInput where, int page, int pageSize, Sort orderBy) {
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

  public List<ArticleInteraction> interactionRecords(Article article) {
    return new ArrayList<>();
  }

  public PermissionStatement permissions(Article article) {
    return null; // Permission.builder().resourceType("Article").id(String.valueOf(article.getId())).build();
  }

  private List<PermissionStatement> loadGrantPermissions(Article article) {
    //        if (article.getPermissions() == null) {
    //            article.setPermissions(securityGrpcInvoke.getPermissions("Article",
    // String.valueOf(article.getId())));
    //        }
    return new ArrayList<>(); // article.getPermissions();
  }
}
