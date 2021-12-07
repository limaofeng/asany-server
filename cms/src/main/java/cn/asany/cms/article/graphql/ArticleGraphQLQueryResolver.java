package cn.asany.cms.article.graphql;

import cn.asany.cms.article.bean.Article;
import cn.asany.cms.article.bean.ArticleChannel;
import cn.asany.cms.article.bean.ArticleTag;
import cn.asany.cms.article.converter.ArticleChannelConverter;
import cn.asany.cms.article.graphql.enums.ArticleChannelStarType;
import cn.asany.cms.article.graphql.enums.ArticleStarType;
import cn.asany.cms.article.graphql.input.ArticleChannelFilter;
import cn.asany.cms.article.graphql.input.ArticleFilter;
import cn.asany.cms.article.graphql.type.ArticleConnection;
import cn.asany.cms.article.service.ArticleChannelService;
import cn.asany.cms.article.service.ArticleService;
import cn.asany.cms.article.service.ArticleTagService;
import cn.asany.cms.permission.specification.StarSpecification;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.Kit;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-04-01 15:17
 */
@Component
public class ArticleGraphQLQueryResolver implements GraphQLQueryResolver {

  private final ArticleService articleService;
  private final ArticleTagService articleTagService;
  private final ArticleChannelService channelService;
  private final ArticleChannelConverter articleChannelConverter;

  public ArticleGraphQLQueryResolver(
      ArticleService articleService,
      ArticleTagService articleTagService,
      ArticleChannelService channelService,
      ArticleChannelConverter articleChannelConverter) {
    this.articleService = articleService;
    this.articleTagService = articleTagService;
    this.channelService = channelService;
    this.articleChannelConverter = articleChannelConverter;
  }

  public Article article(Long id) {
    return articleService.get(id);
  }

  /**
   * 查询所有文章
   *
   * @param filter
   * @param page
   * @param pageSize
   * @param orderBy
   * @return
   */
  public ArticleConnection articles(
      ArticleFilter filter, int first, int page, int pageSize, OrderBy orderBy) {
    PropertyFilterBuilder builder =
        ObjectUtil.defaultValue(filter, new ArticleFilter()).getBuilder();
    Pager<Article> pager = Pager.<Article>builder().pageSize(pageSize).pageSize(pageSize).build();

    pager.setOrderBy(ObjectUtil.defaultValue(orderBy, OrderBy.desc("createdAt")));

    return Kit.connection(
        articleService.findPager(pager, builder.build()), ArticleConnection.class);
  }

  /**
   * 查询所有栏目
   *
   * @return
   */
  public List<ArticleChannel> articleChannels(ArticleChannelFilter filter, OrderBy orderBy) {
    if (orderBy != null) {
      return channelService.findAllArticle(filter.build(), orderBy.toSort());
    } else {
      return channelService.findAll(filter.build());
    }
  }

  private PropertyFilterBuilder propertyFilterBuilder(PropertyFilterBuilder builder) {
    return builder;
  }

  /**
   * 根据ID查询栏目
   *
   * @param id
   * @return
   */
  public Optional<ArticleChannel> articleChannel(Long id) {
    return channelService.get(id);
  }

  public List<ArticleTag> articleTags(
      String organization, ArticleChannelFilter filter, OrderBy orderBy) {
    PropertyFilterBuilder builder =
        ObjectUtil.defaultValue(filter, new ArticleChannelFilter()).getBuilder();
    if (organization != null) {
      builder.equal("organization.id", organization);
    }
    if (orderBy != null) {
      return articleTagService.findAllArticle(builder.build(), orderBy.toSort());
    } else {
      return articleTagService.findAll(builder.build());
    }
  }

  public List<ArticleChannel> starredArticleChannels(
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
      OrderBy orderBy) {
    PropertyFilterBuilder builder =
        ObjectUtil.defaultValue(filter, new ArticleFilter()).getBuilder();
    builder.and(new StarSpecification(employee, starType.getValue()));
    return Kit.connection(
        articleService.findPager(
            new Pager<>(
                page, pageSize, ObjectUtil.defaultValue(orderBy, OrderBy.desc("createdAt"))),
            builder.build()),
        ArticleConnection.class);
  }
}
