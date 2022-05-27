package cn.asany.cms.article.graphql;

import cn.asany.cms.article.converter.ArticleChannelConverter;
import cn.asany.cms.article.domain.Article;
import cn.asany.cms.article.domain.ArticleChannel;
import cn.asany.cms.article.domain.ArticleTag;
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
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.regexp.RegexpConstant;
import org.jfantasy.framework.util.regexp.RegexpUtil;
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

  /**
   * 查询所有栏目
   *
   * @return List<ArticleChannel>
   */
  public List<ArticleChannel> articleChannels(ArticleChannelFilter filter, Sort orderBy) {
    if (orderBy != null) {
      return channelService.findAllArticle(filter.build(), orderBy);
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
   * @param id ID
   * @return Optional<ArticleChannel>
   */
  public Optional<ArticleChannel> articleChannel(String id) {
    if (RegexpUtil.isMatch(id, RegexpConstant.VALIDATOR_INTEGE)) {
      return channelService.get(id);
    }
    return channelService.findOneBySlug(id);
  }

  public List<ArticleTag> articleTags(
      String organization, ArticleChannelFilter filter, Sort orderBy) {
    PropertyFilterBuilder builder =
        ObjectUtil.defaultValue(filter, new ArticleChannelFilter()).getBuilder();
    if (organization != null) {
      builder.equal("organization.id", organization);
    }
    if (orderBy != null) {
      return articleTagService.findAllArticle(builder.build(), orderBy);
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
      Sort orderBy) {
    PropertyFilterBuilder builder =
        ObjectUtil.defaultValue(filter, new ArticleFilter()).getBuilder();
    builder.and(new StarSpecification(employee, starType.getValue()));
    return Kit.connection(
        articleService.findPage(PageRequest.of(page - 1, pageSize, orderBy), builder.build()),
        ArticleConnection.class);
  }
}
