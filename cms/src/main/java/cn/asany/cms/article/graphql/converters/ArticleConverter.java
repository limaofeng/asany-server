package cn.asany.cms.article.graphql.converters;

import cn.asany.cms.article.bean.*;
import cn.asany.cms.article.graphql.inputs.ArticleInput;
import cn.asany.cms.article.graphql.inputs.ContentInput;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

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
  ArticleConverter INSTANCE = Mappers.getMapper(ArticleConverter.class);

  @Mappings({
    @Mapping(source = "content", target = "content", qualifiedByName = "parseContent"),
    @Mapping(source = "channels", target = "channels", qualifiedByName = "parseChannels"),
    @Mapping(
        source = "tags",
        target = "tags", /*qualifiedByName = "parseChannels",*/
        ignore = true),
    @Mapping(source = "recommend", target = "recommend", qualifiedByName = "parseRecommend"),
    @Mapping(target = "permissions", ignore = true)
  })
  Article toArticle(ArticleInput input);

  @Mappings({
    //    @Mapping(source = "content", target = "content", qualifiedByName = "parseContentFile"),
    @Mapping(source = "channels", target = "channels", qualifiedByName = "parseChannels"),
    @Mapping(source = "tags", target = "tags", ignore = true),
    @Mapping(target = "permissions", ignore = true),
    @Mapping(source = "recommend", target = "recommend", qualifiedByName = "parseRecommend"),
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
    //    switch (source.getType()) {
    //      case file:
    //        FileObject videoUrl = source.getVideo();
    //        FileObjectConverter converter = new FileObjectConverter();
    //        return Content.builder()
    //            .type(source.getType())
    //            .text(converter.convertToDatabaseColumn(videoUrl))
    //            .build();
    //      case json:
    //        List<ContentPictureInput> pictures = source.getPictures();
    //        return
    // Content.builder().type(source.getType()).text(JSON.serialize(pictures)).build();
    //      case html:
    //        String html = source.getHtml();
    //        return Content.builder().type(source.getType()).text(html).build();
    //      case link:
    //        String link = source.getLink();
    //        return Content.builder().type(source.getType()).text(link).build();
    //      case markdown:
    //        throw new ValidationException("暂不支持 markdown 格式");
    //
    //      default:
    //        throw new IllegalStateException("Unexpected value: " + source.getType());
    //    }
    return null;
  }

  @Named("parseContentFile")
  default Content parseContentFile(String source) {
    if (source == null) {
      return null;
    }
    return null; // Content.builder().text(source).type(ContentType.file).build();
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

  @Named("parseRecommend")
  default List<ArticleRecommend> parseRecommend(List<Long> source) {
    if (source == null) {
      return null;
    }
    return source.stream()
        .map(
            item ->
                ArticleRecommend.builder().recommend(Recommend.builder().id(item).build()).build())
        .collect(Collectors.toList());
  }
}
