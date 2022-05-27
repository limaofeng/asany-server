package cn.asany.cms.article.converter;

import cn.asany.cms.article.domain.Article;
import cn.asany.cms.article.domain.ArticleChannel;
import cn.asany.cms.article.domain.ArticleTag;
import cn.asany.cms.article.graphql.input.ArticleChannelInput;
import cn.asany.cms.article.graphql.input.ArticleTagInput;
import cn.asany.cms.module.dto.ArticleChannelImpObj;
import cn.asany.cms.module.dto.ArticleImpObj;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.dto.SimpleFileObject;
import java.util.List;
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

  @IterableMapping(elementTargetType = ArticleChannel.class)
  List<ArticleChannel> toChannels(List<ArticleChannelImpObj> channels);

  @Mappings({
    @Mapping(source = "posts", target = "articles"),
  })
  ArticleChannel toChannel(ArticleChannelImpObj channel);

  @Mappings({
    @Mapping(source = "cover", target = "cover", qualifiedByName = "toCoverFromString"),
  })
  Article toArticle(ArticleImpObj articleImpObj);

  @Named("toCoverFromString")
  default FileObject toCoverFromString(String cover) {
    SimpleFileObject simpleFileObject = new SimpleFileObject();
    simpleFileObject.setUrl(cover);
    return simpleFileObject;
  }

  @Mappings({
    @Mapping(source = "slug", target = "slug"),
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
