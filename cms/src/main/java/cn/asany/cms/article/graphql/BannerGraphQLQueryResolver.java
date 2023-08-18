package cn.asany.cms.article.graphql;

import cn.asany.cms.article.domain.Banner;
import cn.asany.cms.article.graphql.input.BannerWhereInput;
import cn.asany.cms.article.service.BannerService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class BannerGraphQLQueryResolver implements GraphQLQueryResolver {

  private final BannerService bannerService;

  public BannerGraphQLQueryResolver(BannerService bannerService) {
    this.bannerService = bannerService;
  }

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
   * @param where 筛选
   * @param orderBy 排序
   * @return List<Banner>
   */
  public List<Banner> banners(BannerWhereInput where, Sort orderBy) {
    return this.bannerService.findAll(where.toFilter(), orderBy);
  }
}
