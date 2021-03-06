package cn.asany.cms.article.converter;

import cn.asany.cms.article.domain.*;
import cn.asany.cms.article.graphql.input.ArticleCategoryInput;
import cn.asany.cms.article.graphql.input.ArticleTagInput;
import cn.asany.cms.article.graphql.input.PageComponentInput;
import cn.asany.cms.module.dto.ArticleChannelImpObj;
import cn.asany.cms.module.dto.ArticleImpObj;
import cn.asany.nuwa.app.domain.ApplicationRoute;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.dto.SimpleFileObject;
import cn.asany.ui.resources.domain.Component;
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
public interface ArticleCategoryConverter {

  @IterableMapping(elementTargetType = ArticleCategory.class)
  List<ArticleCategory> toChannels(List<ArticleChannelImpObj> channels);

  @Mappings({
    //    @Mapping(source = "posts", target = "articles"),
  })
  ArticleCategory toCategory(ArticleChannelImpObj channel);

  @Mappings({
    @Mapping(source = "image", target = "image", qualifiedByName = "toCoverFromString"),
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
    @Mapping(target = "storeTemplate", source = "storeTemplate", qualifiedByName = "storeTemplate"),
    @Mapping(target = "page", source = "page", qualifiedByName = "convertPage"),
    @Mapping(target = "parent", source = "parent", qualifiedByName = "formatChannelParent"),
  })
  ArticleCategory toCategory(ArticleCategoryInput tag);

  @Mappings({
    @Mapping(target = "id", ignore = true),
    @Mapping(target = "path", ignore = true),
    @Mapping(target = "metadata", ignore = true),
  })
  ArticleTag toArticle(ArticleTagInput input);

  @Named("formatChannelParent")
  default ArticleCategory formatChannelParent(Long source) {
    if (source == null) {
      return null;
    }
    return ArticleCategory.builder().id(source).build();
  }

  @Named("storeTemplate")
  default ArticleStoreTemplate storeTemplate(String source) {
    if (source == null) {
      return null;
    }
    return ArticleStoreTemplate.builder().id(source).build();
  }

  @Named("convertPage")
  default PageComponent convertPage(PageComponentInput source) {
    return PageComponent.builder()
        .enabled(source.getEnabled())
        .component(
            Component.builder().template(source.getTemplate()).blocks(source.getBlocks()).build())
        .route(ApplicationRoute.builder().path(source.getPath()).build())
        .build();
  }
}
