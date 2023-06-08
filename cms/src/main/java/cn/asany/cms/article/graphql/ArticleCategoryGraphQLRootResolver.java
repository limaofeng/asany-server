package cn.asany.cms.article.graphql;

import cn.asany.cms.article.converter.ArticleCategoryConverter;
import cn.asany.cms.article.domain.ArticleCategory;
import cn.asany.cms.article.graphql.input.ArticleCategoryWhereInput;
import cn.asany.cms.article.graphql.input.ArticleCategoryInput;
import cn.asany.cms.article.service.ArticleCategoryService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.util.regexp.RegexpConstant;
import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

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
  public List<ArticleCategory> articleCategories(ArticleCategoryWhereInput filter, Sort orderBy) {
    if (orderBy != null) {
      return channelService.findAllArticle(filter.toFilter(), orderBy);
    } else {
      return channelService.findAll(filter.toFilter());
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
