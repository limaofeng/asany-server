package cn.asany.cms.article.graphql;

import cn.asany.cms.article.bean.Article;
import cn.asany.cms.article.bean.ArticleTag;
import cn.asany.cms.article.bean.enums.ArticleTagCategory;
import cn.asany.cms.article.graphql.converters.ArticleChannelConverter;
import cn.asany.cms.article.graphql.enums.ArticleChannelStarType;
import cn.asany.cms.article.graphql.enums.ArticleStarType;
import cn.asany.cms.article.graphql.inputs.ArticleChannelFilter;
import cn.asany.cms.article.graphql.inputs.ArticleFilter;
import cn.asany.cms.article.graphql.types.ArticleChannel;
import cn.asany.cms.article.graphql.types.ArticleConnection;
import cn.asany.cms.article.service.ArticleService;
import cn.asany.cms.article.service.ArticleTagService;
import cn.asany.cms.permission.specification.StarSpecification;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.Kit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-04-01 15:17
 */
@Component
public class ArticleGraphQLQueryResolver implements GraphQLQueryResolver {

  @Autowired private ArticleService articleService;
  @Autowired private ArticleTagService articleTagService;
  @Autowired private ArticleChannelConverter articleChannelConverter;

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
      String organization,
      ArticleFilter filter,
      Boolean isLimit,
      int first,
      int page,
      int pageSize,
      OrderBy orderBy) {
    PropertyFilterBuilder builder =
        ObjectUtil.defaultValue(filter, new ArticleFilter()).getBuilder();
    builder.equal("organization.id", organization);
    Pager<Article> pager = null;
    if (isLimit == true) {
      pager =
          new Pager<>(ObjectUtil.defaultValue(orderBy, OrderBy.desc("createdAt")), first, pageSize);
    } else {
      pager =
          new Pager<>(page, pageSize, ObjectUtil.defaultValue(orderBy, OrderBy.desc("createdAt")));
    }

    return Kit.connection(
        articleService.findPager(pager, builder.build()), ArticleConnection.class);
  }

  /**
   * 查询所有栏目
   *
   * @param organization
   * @return
   */
  public List<ArticleChannel> articleChannels(
      String organization, ArticleChannelFilter filter, OrderBy orderBy) {
    PropertyFilterBuilder builder =
        ObjectUtil.defaultValue(filter, new ArticleChannelFilter()).getBuilder();
    builder.equal("category", ArticleTagCategory.channel);
    if (organization != null) {
      builder.equal("organization.id", organization);
    }
    if (orderBy != null) {
      return articleTagService.findAllArticle(builder.build(), orderBy.toSort()).stream()
          .map(item -> articleChannelConverter.toChannel(item))
          .collect(Collectors.toList());
    } else {
      return articleTagService.findAll(builder.build()).stream()
          .map(item -> articleChannelConverter.toChannel(item))
          .collect(Collectors.toList());
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
  public ArticleChannel articleChannel(Long id) {
    Optional<ArticleTag> optional = articleTagService.get(id);
    if (!optional.isPresent()) return null;
    ArticleChannel articleChannel = articleChannelConverter.toChannel(optional.get());
    articleChannel.setParent(
        articleChannelConverter.formatArticleTagParent(optional.get().getParent()));
    articleChannel.setChildren(
        articleChannelConverter.formatChildren(optional.get().getChildren()));
    return articleChannel;
  }

  public List<ArticleTag> articleTags(
      String organization, ArticleChannelFilter filter, OrderBy orderBy) {
    PropertyFilterBuilder builder =
        ObjectUtil.defaultValue(filter, new ArticleChannelFilter()).getBuilder();
    if (organization != null) {
      builder.equal("organization.id", organization);
    }
    builder.equal("category", ArticleTagCategory.tag);
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
    List<ArticleTag> articleTags = articleTagService.findAll(builder.build());
    return articleTags.stream()
        .map(item -> articleChannelConverter.toChannel(item))
        .collect(Collectors.toList());
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
