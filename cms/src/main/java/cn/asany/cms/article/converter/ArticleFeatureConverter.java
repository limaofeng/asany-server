package cn.asany.cms.article.converter;

import cn.asany.cms.article.domain.ArticleFeature;
import cn.asany.cms.article.graphql.input.ArticleFeatureInput;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * 转换器
 *
 * @author limaofeng
 */
@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ArticleFeatureConverter {

  ArticleFeature convert(ArticleFeatureInput input);
}
