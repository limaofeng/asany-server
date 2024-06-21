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
package cn.asany.cms.article.graphql;

import cn.asany.cms.article.converter.ArticleCategoryConverter;
import cn.asany.cms.article.converter.ArticleConverter;
import cn.asany.cms.article.domain.*;
import cn.asany.cms.article.graphql.input.*;
import cn.asany.cms.article.graphql.type.ArticleConnection;
import cn.asany.cms.article.service.ArticleCategoryService;
import cn.asany.cms.article.service.ArticleService;
import cn.asany.cms.article.service.ArticleTagService;
import cn.asany.cms.content.service.ArticleContentService;
import cn.asany.organization.core.domain.Organization;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.graphql.util.Kit;
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
 */
@Component
public class ArticleGraphQLRootResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final ArticleCategoryConverter articleCategoryConverter;
  private final ArticleConverter articleConverter;
  private final ArticleService articleService;
  private final ArticleTagService articleTagService;
  private final ArticleCategoryService articleCategoryService;
  private final ArticleContentService contentService;

  protected final Environment environment;

  public ArticleGraphQLRootResolver(
      ArticleCategoryConverter articleCategoryConverter,
      ArticleConverter articleConverter,
      ArticleService articleService,
      ArticleTagService articleTagService,
      ArticleCategoryService articleCategoryService,
      ArticleContentService contentService,
      Environment environment) {
    this.articleCategoryConverter = articleCategoryConverter;
    this.articleConverter = articleConverter;
    this.articleService = articleService;
    this.articleTagService = articleTagService;
    this.articleCategoryService = articleCategoryService;
    this.contentService = contentService;
    this.environment = environment;
  }

  public Article article(Long id) {
    return articleService.get(id);
  }

  /**
   * 查询所有文章
   *
   * @param where 过滤
   * @param page 页码
   * @param pageSize 每页显示数据条数
   * @param orderBy 排序
   * @return ArticleConnection
   */
  public ArticleConnection articlesConnection(
      ArticleWhereInput where, int page, int pageSize, Sort orderBy) {
    PropertyFilter queryFilter = ObjectUtil.defaultValue(where, new ArticleWhereInput()).toFilter();

    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);

    return Kit.connection(articleService.findPage(pageable, queryFilter), ArticleConnection.class);
  }

  /**
   * 查询所有文章
   *
   * @param where 过滤
   * @param offset 偏移量
   * @param limit 限制
   * @param orderBy 排序
   * @return List<Article>
   */
  public List<Article> articles(ArticleWhereInput where, int offset, int limit, Sort orderBy) {
    return articleService.findAll(where.toFilter(), offset, limit, orderBy);
  }

  public List<ArticleTag> articleTags(
      String organization, ArticleCategoryWhereInput where, Sort orderBy) {
    PropertyFilter filter = where.toFilter();
    if (organization != null) {
      filter.equal("organization.id", organization);
    }
    if (orderBy != null) {
      return articleTagService.findAllArticle(filter, orderBy);
    } else {
      return articleTagService.findAll(filter);
    }
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

    ArticleStoreTemplate storeTemplate = category.getStoreTemplate();

    Article article = articleConverter.toArticle(input);
    ArticleContent content =
        contentService.convert(input.getContent(), storeTemplate.getContentType());

    article.setOrganization(Organization.builder().id(organizationId).build());
    article.setCategory(category);

    return articleService.save(article, content, input.getPermissions());
  }

  /**
   * 修改文章
   *
   * @param id ID
   * @param merge 合并方式
   * @param input 输入对象
   * @return Article
   */
  public Article updateArticle(Long id, ArticleUpdateInput input, Boolean merge) {
    ArticleCategory category = this.articleCategoryService.getById(input.getCategory());

    ArticleStoreTemplate storeTemplate = category.getStoreTemplate();

    Article article = articleConverter.toArticle(input);
    ArticleContent content =
        contentService.convert(input.getContent(), storeTemplate.getContentType());

    article.setCategory(category);
    article.setContentType(storeTemplate.getContentType());

    return articleService.update(id, article, content, merge);
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
