package cn.asany.cms.article.converter;

import cn.asany.cms.article.domain.Banner;
import cn.asany.cms.article.graphql.input.BannerCreateInput;
import cn.asany.cms.article.graphql.input.BannerUpdateInput;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

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
public interface BannerConverter {

  /**
   * 转换新增对象
   *
   * @param input 输入对象
   * @return Banner
   */
  @Mappings({})
  Banner toBannerFromCreateInput(BannerCreateInput input);

  /**
   * 转换修改对象
   *
   * @param input 输入对象
   * @return Banner
   */
  @Mappings({})
  Banner toBannerFromUpdateInput(BannerUpdateInput input);
}
