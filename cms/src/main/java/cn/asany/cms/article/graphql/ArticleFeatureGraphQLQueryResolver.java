package cn.asany.cms.article.graphql;

import cn.asany.cms.article.bean.ArticleFeature;
import cn.asany.cms.article.converter.ArticleFeatureConverter;
import cn.asany.cms.article.graphql.input.ArticleFeatureFilter;
import cn.asany.cms.article.graphql.input.ArticleFeatureInput;
import cn.asany.cms.article.service.ArticleFeatureService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.stereotype.Component;

/**
 * 文章特征
 *
 * @author ChenWenJie
 * @date 2020/10/22 11:21 上午
 */
@Component
public class ArticleFeatureGraphQLQueryResolver
    implements GraphQLQueryResolver, GraphQLMutationResolver {
  private final ArticleFeatureConverter articleFeatureConverter;
  private final ArticleFeatureService featureService;

  public ArticleFeatureGraphQLQueryResolver(
      ArticleFeatureConverter articleFeatureConverter, ArticleFeatureService featureService) {
    this.articleFeatureConverter = articleFeatureConverter;
    this.featureService = featureService;
  }

  public List<ArticleFeature> articleFeatures(
      String organization, ArticleFeatureFilter filter, OrderBy orderBy) {
    PropertyFilterBuilder builder =
        ObjectUtil.defaultValue(filter, new ArticleFeatureFilter()).getBuilder();
    builder.equal("organization.id", organization);
    if (orderBy == null) {
      orderBy = OrderBy.desc("createdAt");
    }
    return featureService.findAll(builder.build(), orderBy.toSort());
  }

  public ArticleFeature articleFeature(Long id) {
    return featureService.findById(id);
  }

  public ArticleFeature createArticleFeature(ArticleFeatureInput input) {
    return featureService.createArticleFeature(articleFeatureConverter.convert(input));
  }

  public ArticleFeature updateArticleFeature(Long id, Boolean merge, ArticleFeatureInput input) {
    return featureService.updateArticleFeature(id, merge, articleFeatureConverter.convert(input));
  }

  public Boolean deleteArticleFeature(Long id) {
    featureService.deleteArticleFeature(id);
    return true;
  }
}
