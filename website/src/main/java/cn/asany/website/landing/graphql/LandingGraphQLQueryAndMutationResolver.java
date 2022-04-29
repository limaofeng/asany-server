package cn.asany.website.landing.graphql;

import cn.asany.website.landing.bean.LandingPage;
import cn.asany.website.landing.bean.LandingPoster;
import cn.asany.website.landing.bean.LandingStore;
import cn.asany.website.landing.convert.PageConverter;
import cn.asany.website.landing.convert.PosterConverter;
import cn.asany.website.landing.convert.StoreConverter;
import cn.asany.website.landing.graphql.input.*;
import cn.asany.website.landing.graphql.type.LandingPageConnection;
import cn.asany.website.landing.graphql.type.LandingPosterConnection;
import cn.asany.website.landing.graphql.type.LandingStoreConnection;
import cn.asany.website.landing.service.LandingService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.Optional;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.Kit;
import org.springframework.stereotype.Component;

@Component
public class LandingGraphQLQueryAndMutationResolver
    implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final StoreConverter storeConverter;
  final PageConverter pageConverter;
  final PosterConverter posterConverter;
  private final LandingService landingService;

  public LandingGraphQLQueryAndMutationResolver(
      LandingService landingService,
      StoreConverter storeConverter,
      PageConverter pageConverter,
      PosterConverter posterConverter) {
    this.landingService = landingService;
    this.storeConverter = storeConverter;
    this.pageConverter = pageConverter;
    this.posterConverter = posterConverter;
  }

  public LandingPage createLandingPage(LandingPageCreateInput input) {
    LandingPage page = pageConverter.toPage(input);
    return this.landingService.save(page);
  }

  public LandingPage updateLandingPage(Long id, LandingPageUpdateInput input, boolean merge) {
    LandingPage page = pageConverter.toPage(input);
    return this.landingService.update(id, page, merge);
  }

  public int deleteLandingPage(Long... ids) {
    return this.landingService.deletePage(ids);
  }

  public Optional<LandingPage> landingPage(Long id) {
    return this.landingService.findPage(id);
  }

  public LandingPageConnection landingPagesConnection(
      LandingPageFilter filter, int page, int pageSize, OrderBy orderBy) {
    filter = ObjectUtil.defaultValue(filter, new LandingPageFilter());
    Pager<LandingPage> pager = Pager.newPager(page, pageSize, orderBy);
    return Kit.connection(
        landingService.findLandingPagePager(pager, filter.build()), LandingPageConnection.class);
  }

  public LandingPoster createLandingPoster(LandingPosterCreateInput input) {
    LandingPoster poster = posterConverter.toPoster(input);
    return this.landingService.save(poster);
  }

  public LandingPoster updateLandingPoster(Long id, LandingPosterUpdateInput input, boolean merge) {
    LandingPoster poster = posterConverter.toPoster(input);
    return this.landingService.update(id, poster, merge);
  }

  public int deleteLandingPoster(Long... ids) {
    return this.landingService.deletePoster(ids);
  }

  public Optional<LandingPoster> landingPoster(Long id) {
    return this.landingService.findPoster(id);
  }

  public LandingPosterConnection landingPostersConnection(
      LandingPosterFilter filter, int page, int pageSize, OrderBy orderBy) {
    filter = ObjectUtil.defaultValue(filter, new LandingPosterFilter());
    Pager<LandingPoster> pager = Pager.newPager(page, pageSize, orderBy);
    return Kit.connection(
        landingService.findLandingPosterPager(pager, filter.build()),
        LandingPosterConnection.class);
  }

  public LandingStore createLandingStore(LandingStoreCreateInput input) {
    LandingStore store = storeConverter.toStore(input);
    return this.landingService.save(store);
  }

  public LandingStore updateLandingStore(Long id, LandingStoreUpdateInput input, boolean merge) {
    LandingStore store = storeConverter.toStore(input);
    return this.landingService.update(id, store, merge);
  }

  public int deleteLandingStore(Long... ids) {
    return this.landingService.deleteStore(ids);
  }

  public Optional<LandingStore> landingStore(Long id) {
    return this.landingService.findStore(id);
  }

  public LandingStoreConnection landingStoresConnection(
      LandingStoreFilter filter, int page, int pageSize, OrderBy orderBy) {
    filter = ObjectUtil.defaultValue(filter, new LandingStoreFilter());
    Pager<LandingStore> pager = Pager.newPager(page, pageSize, orderBy);
    return Kit.connection(
        landingService.findLandingStorePager(pager, filter.build()), LandingStoreConnection.class);
  }
}
