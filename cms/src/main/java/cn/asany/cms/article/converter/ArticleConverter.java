package cn.asany.cms.article.converter;

import cn.asany.cms.article.domain.Article;
import cn.asany.cms.article.domain.ArticleBody;
import cn.asany.cms.article.domain.ArticleCategory;
import cn.asany.cms.article.domain.ArticleFeature;
import cn.asany.cms.article.graphql.input.ArticleCreateInput;
import cn.asany.cms.article.graphql.input.ArticleUpdateInput;
import cn.asany.cms.article.service.ArticleFeatureService;
import cn.asany.cms.body.service.ArticleBodyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jfantasy.framework.spring.SpringBeanUtils;
import org.mapstruct.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 文章转换
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-08-15 09:50
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ArticleConverter {

  @Mappings({
    @Mapping(source = "body", target = "body", qualifiedByName = "parseArticleBody"),
    @Mapping(source = "category", target = "category", qualifiedByName = "parseArticleCategory"),
    @Mapping(source = "tags", target = "tags", ignore = true),
    @Mapping(source = "features", target = "features", qualifiedByName = "parseFeature"),
    @Mapping(target = "permissions", ignore = true),
    @Mapping(target = "organization", ignore = true)
  })
  Article toArticle(ArticleCreateInput input, @Context ArticleContext context);

  @Mappings({
    @Mapping(source = "body", target = "body", qualifiedByName = "parseArticleBody"),
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

  @Named("parseArticleBody")
  default ArticleBody parseArticleBody(String bodyInput, @Context ArticleContext context)
      throws JsonProcessingException {
    if (bodyInput == null) {
      return null;
    }
    ArticleBodyService bodyService = SpringBeanUtils.getBean(ArticleBodyService.class);
    return bodyService.convert(bodyInput, context.getStoreTemplate());
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
