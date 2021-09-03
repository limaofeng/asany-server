package cn.asany.cms.article.graphql.converters;

import cn.asany.cms.article.bean.ArticleTag;
import cn.asany.cms.article.graphql.inputs.ArticleChannelInput;
import cn.asany.cms.article.graphql.inputs.ArticleTagInput;
import cn.asany.cms.article.graphql.types.ArticleChannel;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * 文章栏目转换器
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-08-15 09:50
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleChannelConverter {
  ArticleChannelConverter INSTANCE = Mappers.getMapper(ArticleChannelConverter.class);

  @Mappings({
    @Mapping(source = "code", target = "url"),
    @Mapping(target = "children", source = "children", qualifiedByName = "formatChildren"),
    @Mapping(target = "parent", ignore = true),
  })
  ArticleChannel toChannel(ArticleTag tag);

  @Mappings({
    @Mapping(source = "url", target = "code"),
    @Mapping(target = "id", ignore = true),
    @Mapping(target = "path", ignore = true),
    @Mapping(target = "category", ignore = true),
    @Mapping(target = "parent", source = "parent", qualifiedByName = "formatParent"),
    @Mapping(target = "permissions", ignore = true)
  })
  ArticleTag toChannel(ArticleChannelInput tag);

  @Mappings({
    @Mapping(target = "id", ignore = true),
    @Mapping(target = "path", ignore = true),
    @Mapping(target = "category", ignore = true),
    @Mapping(target = "meta", ignore = true),
  })
  ArticleTag toArticle(ArticleTagInput input);

  @Named("formatArticleTagParent")
  default ArticleChannel formatArticleTagParent(ArticleTag parent) {
    if (parent == null) {
      return null;
    }
    return toChannel(parent);
  }

  @Named("formatParent")
  default ArticleTag formatParent(Long source) {
    if (source == null) {
      return null;
    }
    return ArticleTag.builder().id(source).build();
  }

  @Named("formatChildren")
  default List<ArticleChannel> formatChildren(List<ArticleTag> children) {
    if (children == null || children.size() < 1) {
      return null;
    }
    List<ArticleChannel> channels = new ArrayList<ArticleChannel>();
    for (ArticleTag child : children) {
      channels.add(toChannel(child));
    }
    return channels;
  }
}
