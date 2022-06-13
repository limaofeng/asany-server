package cn.asany.cms.article.graphql;

import cn.asany.cms.article.domain.Article;
import cn.asany.cms.article.domain.ArticleCategory;
import cn.asany.cms.article.domain.ArticleTag;
import cn.asany.cms.article.graphql.enums.ArticleChannelStarType;
import cn.asany.cms.article.graphql.enums.ArticleStarType;
import cn.asany.cms.article.graphql.input.ArticleCategoryFilter;
import cn.asany.cms.article.graphql.input.ArticleFilter;
import cn.asany.cms.article.graphql.type.ArticleConnection;
import cn.asany.cms.article.service.ArticleCategoryService;
import cn.asany.cms.article.service.ArticleService;
import cn.asany.cms.article.service.ArticleTagService;
import cn.asany.cms.permission.specification.StarSpecification;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng
 * @version V1.0
 * @date 2019-04-01 15:17
 */
@Slf4j
@Component
public class ArticleGraphQLQueryResolver implements GraphQLQueryResolver {

  private final ArticleService articleService;
  private final ArticleTagService articleTagService;
  private final ArticleCategoryService channelService;

  public ArticleGraphQLQueryResolver(
      ArticleService articleService,
      ArticleTagService articleTagService,
      ArticleCategoryService channelService) {
    this.articleService = articleService;
    this.articleTagService = articleTagService;
    this.channelService = channelService;
  }

  public Article article(Long id) {
    return articleService.get(id);
  }

  /**
   * 查询所有文章
   *
   * @param filter 过滤
   * @param page 页码
   * @param pageSize 每页显示数据条数
   * @param orderBy 排序
   * @return ArticleConnection
   */
  public ArticleConnection articles(
      ArticleFilter filter, int first, int page, int pageSize, Sort orderBy) {
    PropertyFilterBuilder builder =
        ObjectUtil.defaultValue(filter, new ArticleFilter()).getBuilder();

    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);

    return Kit.connection(
        articleService.findPage(pageable, builder.build()), ArticleConnection.class);
  }

  public List<ArticleTag> articleTags(
      String organization, ArticleCategoryFilter filter, Sort orderBy) {
    PropertyFilterBuilder builder =
        ObjectUtil.defaultValue(filter, new ArticleCategoryFilter()).getBuilder();
    if (organization != null) {
      builder.equal("organization.id", organization);
    }
    if (orderBy != null) {
      return articleTagService.findAllArticle(builder.build(), orderBy);
    } else {
      return articleTagService.findAll(builder.build());
    }
  }

  public List<ArticleCategory> starredArticleCategories(
      Long employee, ArticleChannelStarType starType) {
    PropertyFilterBuilder builder = PropertyFilter.builder();
    builder.and(new StarSpecification(employee, starType.getValue()));
    return channelService.findAll(builder.build());
  }

  public ArticleConnection starredArticles(
      Long employee,
      ArticleStarType starType,
      ArticleFilter filter,
      int page,
      int pageSize,
      Sort orderBy) {
    PropertyFilterBuilder builder =
        ObjectUtil.defaultValue(filter, new ArticleFilter()).getBuilder();
    builder.and(new StarSpecification(employee, starType.getValue()));
    return Kit.connection(
        articleService.findPage(PageRequest.of(page - 1, pageSize, orderBy), builder.build()),
        ArticleConnection.class);
  }
}
