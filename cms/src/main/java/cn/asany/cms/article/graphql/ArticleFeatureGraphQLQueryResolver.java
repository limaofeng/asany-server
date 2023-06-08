package cn.asany.cms.article.graphql;

import cn.asany.cms.article.converter.ArticleFeatureConverter;
import cn.asany.cms.article.domain.ArticleFeature;
import cn.asany.cms.article.graphql.input.ArticleFeatureWhereInput;
import cn.asany.cms.article.graphql.input.ArticleFeatureInput;
import cn.asany.cms.article.service.ArticleFeatureService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * 文章特征
 *
 * @author ChenWenJie
 * @date 2022/7/28 9:12 9:12
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
    String organization, ArticleFeatureWhereInput where, Sort orderBy) {
    where.toFilter().equal("organization.id", organization);
    return featureService.findAll(where.toFilter(), orderBy);
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
