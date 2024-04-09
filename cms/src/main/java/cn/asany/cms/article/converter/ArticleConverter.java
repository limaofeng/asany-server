package cn.asany.cms.article.converter;

import cn.asany.cms.article.domain.Article;
import cn.asany.cms.article.domain.ArticleCategory;
import cn.asany.cms.article.domain.ArticleContent;
import cn.asany.cms.article.domain.ArticleFeature;
import cn.asany.cms.article.graphql.input.ArticleCreateInput;
import cn.asany.cms.article.graphql.input.ArticleUpdateInput;
import cn.asany.cms.article.service.ArticleFeatureService;
import cn.asany.cms.content.graphql.input.ArticleContentInput;
import cn.asany.cms.content.service.ArticleContentService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jfantasy.framework.spring.SpringBeanUtils;
import org.mapstruct.*;

/**
 * 文章转换
 *
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ArticleConverter {

  @Mappings({
    @Mapping(source = "content", target = "content", qualifiedByName = "parseArticleContent"),
    @Mapping(source = "category", target = "category", qualifiedByName = "parseArticleCategory"),
    @Mapping(source = "tags", target = "tags", ignore = true),
    @Mapping(source = "features", target = "features", qualifiedByName = "parseFeature"),
    @Mapping(target = "permissions", ignore = true),
    @Mapping(target = "organization", ignore = true)
  })
  Article toArticle(ArticleCreateInput input, @Context ArticleContext context);

  @Mappings({
    @Mapping(source = "content", target = "content", qualifiedByName = "parseArticleContent"),
    @Mapping(source = "category", target = "category", qualifiedByName = "parseArticleCategory"),
    @Mapping(source = "tags", target = "tags", ignore = true),
    @Mapping(source = "features", target = "features", qualifiedByName = "parseFeature"),
    @Mapping(target = "permissions", ignore = true),
    @Mapping(target = "organization", ignore = true)
  })
  Article toArticle(ArticleUpdateInput input, @Context ArticleContext context);

  @ObjectFactory
  default Article toUserAddressList(ArticleCreateInput input, @TargetType Class<Article> type) {
    return new Article();
  }

  @Named("parseArticleContent")
  default ArticleContent parseArticleContent(
      ArticleContentInput contentInput, @Context ArticleContext context) {
    if (contentInput == null) {
      return null;
    }
    ArticleContentService contentService = SpringBeanUtils.getBean(ArticleContentService.class);
    return contentService.convert(contentInput, context.getContentType());
  }

  @Named("parseArticleCategory")
  default ArticleCategory parseArticleCategory(Long source) {
    if (source == null) {
      return null;
    }
    return ArticleCategory.builder().id(source).build();
  }

  @Named("parseFeature")
  default List<ArticleFeature> parseFeature(List<String> source) {
    if (source == null) {
      return null;
    }
    ArticleFeatureService articleFeatureService =
        SpringBeanUtils.getBeanByType(ArticleFeatureService.class);
    return source.stream()
        .map(articleFeatureService::findByCode)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
  }
}
