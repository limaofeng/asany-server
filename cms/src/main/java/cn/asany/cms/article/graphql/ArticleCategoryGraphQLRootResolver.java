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
import cn.asany.cms.article.domain.ArticleCategory;
import cn.asany.cms.article.graphql.input.ArticleCategoryInput;
import cn.asany.cms.article.graphql.input.ArticleCategoryWhereInput;
import cn.asany.cms.article.service.ArticleCategoryService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.util.regexp.RegexpConstant;
import net.asany.jfantasy.framework.util.regexp.RegexpUtil;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * 栏目
 *
 * @author limaofeng
 */
@Component
public class ArticleCategoryGraphQLRootResolver
    implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final ArticleCategoryService channelService;

  private final ArticleCategoryConverter categoryConverter;

  public ArticleCategoryGraphQLRootResolver(
      ArticleCategoryService channelService, ArticleCategoryConverter categoryConverter) {
    this.channelService = channelService;
    this.categoryConverter = categoryConverter;
  }

  /**
   * 查询所有栏目
   *
   * @return List<ArticleCategory>
   */
  public List<ArticleCategory> articleCategories(ArticleCategoryWhereInput where, Sort orderBy) {
    if (orderBy != null) {
      return channelService.findAllArticle(where.toFilter(), orderBy);
    } else {
      return channelService.findAll(where.toFilter());
    }
  }

  /**
   * 根据ID查询栏目
   *
   * @param id ID
   * @return Optional<ArticleCategory>
   */
  public Optional<ArticleCategory> articleCategory(String id) {
    if (RegexpUtil.isMatch(id, RegexpConstant.VALIDATOR_INTEGE)) {
      return channelService.findById(Long.parseLong(id));
    }
    return channelService.findOneBySlug(id);
  }

  /**
   * 新增栏目
   *
   * @param input 新增对象
   * @return ArticleCategory
   */
  public ArticleCategory createArticleCategory(ArticleCategoryInput input) {
    ArticleCategory channel = categoryConverter.toCategory(input);
    Long parent = input.getParent();
    if (parent != null) {
      ArticleCategory articleCategory = channelService.findOne(parent);
      channel.setParent(articleCategory);
    }
    channel = channelService.save(channel);
    // 保存权限
    //    if (input.getPermissions() != null) {
    //      channel1.setPermissions(
    //          securityGrpcInvoke.updateGrantPermissions(
    //              "ArticleCategory", channel1.getId(), input.getPermissions()));
    //    }
    return channel;
  }

  /**
   * 更新栏目
   *
   * @param id 栏目ID
   * @param merge 合并模式
   * @param input 更新对象
   * @return ArticleCategory
   */
  public ArticleCategory updateArticleCategory(Long id, Boolean merge, ArticleCategoryInput input) {
    ArticleCategory category = categoryConverter.toCategory(input);
    return channelService.update(id, category, merge);
  }

  /**
   * 删除栏目
   *
   * @param id 栏目ID
   * @return Boolean
   */
  public Boolean deleteArticleCategory(Long id) {
    return channelService.delete(id);
  }
}
