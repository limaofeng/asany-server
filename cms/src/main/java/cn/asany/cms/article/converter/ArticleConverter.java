package cn.asany.cms.article.converter;

import cn.asany.cms.article.domain.*;
import cn.asany.cms.article.domain.enums.ArticleBodyType;
import cn.asany.cms.article.domain.enums.ContentType;
import cn.asany.cms.article.graphql.ArticleBodyInput;
import cn.asany.cms.article.graphql.input.ArticleInput;
import cn.asany.cms.article.graphql.input.ContentInput;
import cn.asany.cms.article.service.ArticleFeatureService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
  Article toArticle(ArticleInput input);

  @ObjectFactory
  default Article toUserAddressList(ArticleInput input, @TargetType Class<Article> type) {
    return new Article();
  }

  @Named("parseArticleBody")
  default ArticleBody parseArticleBody(ArticleBodyInput bodyInput) throws JsonProcessingException {
    if (bodyInput == null) {
      return null;
    }
    if (bodyInput.getType() == ArticleBodyType.classic) {
      return null;
      //      return HtmlArticleBody.builder().text(source.getText()).build();
    }
    throw new IllegalStateException("Unexpected value: " + bodyInput.getType());
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
