package cn.asany.cms.article.graphql;

import cn.asany.cms.article.converter.ArticleCategoryConverter;
import cn.asany.cms.article.converter.ArticleContext;
import cn.asany.cms.article.converter.ArticleConverter;
import cn.asany.cms.article.domain.Article;
import cn.asany.cms.article.domain.ArticleCategory;
import cn.asany.cms.article.domain.ArticleTag;
import cn.asany.cms.article.graphql.enums.ArticleChannelStarType;
import cn.asany.cms.article.graphql.enums.ArticleStarType;
import cn.asany.cms.article.graphql.input.*;
import cn.asany.cms.article.graphql.type.ArticleConnection;
import cn.asany.cms.article.service.ArticleCategoryService;
import cn.asany.cms.article.service.ArticleService;
import cn.asany.cms.article.service.ArticleTagService;
import cn.asany.cms.permission.specification.StarSpecification;
import cn.asany.organization.core.domain.Organization;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import org.jfantasy.framework.dao.jpa.JpaDefaultPropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.Kit;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * 文章接口
 *
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
@Component
public class ArticleGraphQLRootResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final ArticleCategoryConverter articleCategoryConverter;
  private final ArticleConverter articleConverter;
  private final ArticleService articleService;
  private final ArticleTagService articleTagService;
  private final ArticleCategoryService articleCategoryService;
  protected final Environment environment;

  public ArticleGraphQLRootResolver(
      ArticleCategoryConverter articleCategoryConverter,
      ArticleConverter articleConverter,
      ArticleService articleService,
      ArticleTagService articleTagService,
      ArticleCategoryService articleCategoryService,
      Environment environment) {
    this.articleCategoryConverter = articleCategoryConverter;
    this.articleConverter = articleConverter;
    this.articleService = articleService;
    this.articleTagService = articleTagService;
    this.articleCategoryService = articleCategoryService;
    this.environment = environment;
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
    ArticleWhereInput filter, int first, int page, int pageSize, Sort orderBy) {
    PropertyFilter queryFilter =
        ObjectUtil.defaultValue(filter, new ArticleWhereInput()).toFilter();

    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);

    return Kit.connection(
        articleService.findPage(pageable, queryFilter), ArticleConnection.class);
  }

  public List<ArticleTag> articleTags(
    String organization, ArticleCategoryWhereInput filter, Sort orderBy) {
    PropertyFilter builder =
        ObjectUtil.defaultValue(filter, new ArticleCategoryWhereInput()).toFilter();
    if (organization != null) {
      builder.equal("organization.id", organization);
    }
    if (orderBy != null) {
      return articleTagService.findAllArticle(builder, orderBy);
    } else {
      return articleTagService.findAll(builder);
    }
  }

  public List<ArticleCategory> starredArticleCategories(
      Long employee, ArticleChannelStarType starType) {
    PropertyFilter builder = PropertyFilter.newFilter().and(new StarSpecification(employee, starType.getValue()));
    return articleCategoryService.findAll(builder);
  }

  public ArticleConnection starredArticles(
      Long employee,
      ArticleStarType starType,
      ArticleWhereInput filter,
      int page,
      int pageSize,
      Sort orderBy) {
    JpaDefaultPropertyFilter propertyFilter =
      (JpaDefaultPropertyFilter)ObjectUtil.defaultValue(filter, new ArticleWhereInput()).toFilter();
    propertyFilter.and(new StarSpecification(employee, starType.getValue()));
    return Kit.connection(
        articleService.findPage(PageRequest.of(page - 1, pageSize, orderBy), propertyFilter),
        ArticleConnection.class);
  }

  /**
   * 保存文章
   *
   * @param input ArticleCreateInput
   * @return Article
   */
  public Article createArticle(ArticleCreateInput input) {
    Long organizationId = 36L;
    // SpringSecurityUtils.getCurrentUser().getAttribute("organization_id");

    ArticleCategory category = this.articleCategoryService.getById(input.getCategory());

    String storeTemplateId = category.getStoreTemplate().getId();
    ArticleContext articleContext = ArticleContext.builder().storeTemplate(storeTemplateId).build();

    Article article = articleConverter.toArticle(input, articleContext);

    article.setOrganization(Organization.builder().id(organizationId).build());
    article.setCategory(category);

    return articleService.save(article, input.getPermissions());
  }

  /**
   * 修改文章
   *
   * @param id ID
   * @param merge 合并方式
   * @param input 输入对象
   * @return Article
   */
  public Article updateArticle(Long id, Boolean merge, ArticleUpdateInput input) {
    ArticleCategory category = this.articleCategoryService.getById(input.getCategory());

    ArticleContext articleContext =
        ArticleContext.builder().storeTemplate(category.getStoreTemplate().getId()).build();

    Article article = articleConverter.toArticle(input, articleContext);
    article.setCategory(category);

    return articleService.update(id, article, merge);
  }

  /**
   * 删除文章
   *
   * @param id Long
   * @return Boolean
   */
  public Boolean deleteArticle(Long id) {
    return articleService.deleteArticle(id) == 1;
  }

  public Long deleteManyArticles(List<Long> ids) {
    Long[] longs = new Long[ids.size()];
    return articleService.deleteArticle(ids.toArray(longs));
  }

  /**
   * 发布文章
   *
   * @param id
   * @return
   */
  public Boolean publishArticle(Long id) {
    articleService.publish(id);
    return true;
  }

  /**
   * 添加标签
   *
   * @param input
   * @return
   */
  public ArticleTag createArticleTag(ArticleTagInput input) {
    ArticleTag articleTag = articleCategoryConverter.toArticle(input);
    return articleTagService.save(articleTag);
  }
  /**
   * 更新标签
   *
   * @param id
   * @param merge
   * @param input
   * @return
   */
  public ArticleTag updateArticleTag(Long id, Boolean merge, ArticleTagInput input) {
    ArticleTag channel = articleCategoryConverter.toArticle(input);
    return articleTagService.update(id, merge, channel);
  }

  /**
   * 删除标签
   *
   * @param id
   * @return
   */
  public Boolean removeArticleTag(Long id) {
    return articleTagService.delete(id);
  }
}
