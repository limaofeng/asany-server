package cn.asany.cms.article.converter;

import cn.asany.cms.article.bean.ArticleChannel;
import cn.asany.cms.article.bean.ArticleTag;
import cn.asany.cms.article.graphql.input.ArticleChannelInput;
import cn.asany.cms.article.graphql.input.ArticleTagInput;
import org.mapstruct.*;

/**
 * 文章栏目转换器
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-08-15 09:50
 */
@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ArticleChannelConverter {

  @Mappings({
    @Mapping(source = "url", target = "code"),
    @Mapping(target = "id", ignore = true),
    @Mapping(target = "path", ignore = true),
    @Mapping(target = "parent", source = "parent", qualifiedByName = "formatChannelParent"),
  })
  ArticleChannel toChannel(ArticleChannelInput tag);

  @Mappings({
    @Mapping(target = "id", ignore = true),
    @Mapping(target = "path", ignore = true),
    @Mapping(target = "meta", ignore = true),
  })
  ArticleTag toArticle(ArticleTagInput input);

  @Named("formatChannelParent")
  default ArticleChannel formatChannelParent(Long source) {
    if (source == null) {
      return null;
    }
    return ArticleChannel.builder().id(source).build();
  }
}
