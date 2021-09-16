package cn.asany.cms.article.graphql;

import cn.asany.cms.article.bean.Banner;
import cn.asany.cms.article.converter.BannerConverter;
import cn.asany.cms.article.graphql.input.BannerCreateInput;
import cn.asany.cms.article.graphql.input.BannerUpdateInput;
import cn.asany.cms.article.service.BannerService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class BannerGraphQLMutationResolver implements GraphQLMutationResolver {

  private final BannerService bannerService;
  private final BannerConverter bannerConverter;

  public BannerGraphQLMutationResolver(
      BannerService bannerService, BannerConverter bannerConverter) {
    this.bannerService = bannerService;
    this.bannerConverter = bannerConverter;
  }

  /**
   * 创建 Banner
   *
   * @param input 新增对象
   * @return Banner
   */
  Banner createBanner(BannerCreateInput input) {
    return this.bannerService.save(this.bannerConverter.toBannerFromCreateInput(input));
  }

  /**
   * 更新 Banner
   *
   * @param id ID
   * @param input 修改表单
   * @param merge 是否合并
   * @return Banner
   */
  Banner updateBanner(Long id, BannerUpdateInput input, boolean merge) {
    return this.bannerService.update(
        id, this.bannerConverter.toBannerFromUpdateInput(input), merge);
  }

  /**
   * 删除 Banner
   *
   * @param ids 将要删除的数据的 ID
   * @return Boolean
   */
  Boolean deleteBanner(Set<Long> ids) {
    return this.bannerService.delete(ids);
  }
}
