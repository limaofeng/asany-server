package cn.asany.cms.article.graphql;

import cn.asany.cms.article.bean.Banner;
import cn.asany.cms.article.graphql.input.BannerFilter;
import cn.asany.cms.article.service.BannerService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class BannerGraphQLQueryResolver implements GraphQLQueryResolver {

  @Autowired private BannerService bannerService;

  /**
   * 横幅广告
   *
   * @param id ID
   * @return Banner
   */
  public Optional<Banner> banner(Long id) {
    return bannerService.findById(id);
  }

  /**
   * 横幅广告
   *
   * @param filter 筛选
   * @param orderBy 排序
   * @return List<Banner>
   */
  public List<Banner> banners(BannerFilter filter, Sort orderBy) {
    return this.bannerService.findAll(filter.build(), orderBy);
  }
}
