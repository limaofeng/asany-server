package cn.asany.cms.article.graphql.resolver;

import cn.asany.cms.article.domain.Article;
import cn.asany.cms.article.domain.ArticleCategory;
import cn.asany.cms.article.graphql.input.AcceptArticleCategory;
import cn.asany.cms.article.graphql.input.ArticleFilter;
import cn.asany.cms.article.graphql.type.Starrable;
import cn.asany.cms.article.service.ArticleCategoryService;
import cn.asany.cms.article.service.ArticleService;
import cn.asany.cms.permission.domain.Permission;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.jfantasy.framework.dao.LimitPageRequest;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * 文章栏目 解析器
 *
 * @author limaofeng
 * @date 2019-08-15 09:53
 */
@Component
public class ArticleCategoryGraphQLResolver implements GraphQLResolver<ArticleCategory> {

  private final ArticleCategoryService articleCategoryService;
  private final ArticleService articleService;

  public ArticleCategoryGraphQLResolver(
      ArticleCategoryService articleCategoryService, ArticleService articleService) {
    this.articleCategoryService = articleCategoryService;
    this.articleService = articleService;
  }

  public Starrable starrable(ArticleCategory channel) {
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

  public String fullName(ArticleCategory category) {
    List<ArticleCategory> parents = this.parents(category);
    if (parents.isEmpty()) {
      return category.getName();
    }
    return parents.stream().map(ArticleCategory::getName).collect(Collectors.joining("/"))
        + "/"
        + category.getName();
  }

  public List<ArticleCategory> parents(ArticleCategory category) {
    List<Long> ids =
        Arrays.stream(
                StringUtil.tokenizeToStringArray(category.getPath(), ArticleCategory.SEPARATOR))
            .map(Long::valueOf)
            .filter(item -> !item.equals(category.getId()))
            .collect(Collectors.toList());

    if (ids.isEmpty()) {
      return Collections.emptyList();
    }

    List<ArticleCategory> parents =
        this.articleCategoryService.findAll(PropertyFilter.builder().in("id", ids).build());

    return ids.stream().map(id -> ObjectUtil.find(parents, "id", id)).collect(Collectors.toList());
  }

  public List<Article> articles(
      ArticleCategory category,
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
      Sort orderBy) {

    Pageable pageable = LimitPageRequest.of(skip, first, orderBy);

    PropertyFilterBuilder builder = PropertyFilter.builder();

    if (filter != null) {
      builder.and(filter.getBuilder());
    }

    assert filter != null;
    filter.setCategory(
        AcceptArticleCategory.builder().id(category.getId().toString()).subColumns(false).build());

    return this.articleService.findPage(pageable, builder.build()).getContent();
  }

  public Permission permissions(ArticleCategory channel) {
    return Permission.builder()
        .resourceType("ArticleChannel")
        .id(String.valueOf(channel.getId()))
        .build();
  }
}
