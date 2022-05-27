package cn.asany.cms.article.converter;

import cn.asany.cms.article.domain.*;
import cn.asany.cms.article.graphql.input.ArticleInput;
import cn.asany.cms.article.graphql.input.ContentInput;
import cn.asany.cms.article.service.ArticleFeatureService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
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
    @Mapping(source = "content", target = "content", qualifiedByName = "parseContent"),
    @Mapping(source = "channels", target = "channels", qualifiedByName = "parseChannels"),
    @Mapping(source = "tags", target = "tags", ignore = true),
    @Mapping(source = "features", target = "features", qualifiedByName = "parseFeature"),
    @Mapping(target = "permissions", ignore = true)
  })
  Article toArticle(ArticleInput input);

  @Mappings({
    @Mapping(source = "channels", target = "channels", qualifiedByName = "parseChannels"),
    @Mapping(source = "tags", target = "tags", ignore = true),
    @Mapping(target = "permissions", ignore = true),
    @Mapping(source = "features", target = "features", qualifiedByName = "parseFeature"),
    @Mapping(source = "content", target = "content", qualifiedByName = "parseContent")
  })
  Article toArticleFile(ArticleInput input);

  @ObjectFactory
  default Article toUserAddressList(ArticleInput input, @TargetType Class<Article> type) {
    return new Article();
  }

  @Named("parseContent")
  default Content parseContent(ContentInput source) throws JsonProcessingException {
    if (source == null) {
      return null;
    }
    switch (source.getType()) {
      case HTML:
        return HtmlContent.builder().text(source.getText()).build();
      default:
        throw new IllegalStateException("Unexpected value: " + source.getType());
    }
  }

  @Named("parseChannels")
  default List<ArticleChannel> parseChannels(List<Long> source) {
    if (source == null) {
      return null;
    }
    return source.stream()
        .map(item -> ArticleChannel.builder().id(item).build())
        .collect(Collectors.toList());
  }

  @Named("parseFeature")
  default List<ArticleFeature> parseFeature(List<String> source) {
    if (source == null) {
      return null;
    }
    ArticleFeatureService articleFeatureService =
        SpringBeanUtils.getBeanByType(ArticleFeatureService.class);
    return source.stream()
        .map(item -> articleFeatureService.findByCode(item))
        .filter(item -> item.isPresent())
        .map(item -> item.get())
        .collect(Collectors.toList());
  }
}
