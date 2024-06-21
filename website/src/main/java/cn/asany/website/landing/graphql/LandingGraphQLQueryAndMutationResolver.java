/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.website.landing.graphql;

import cn.asany.website.landing.convert.PageConverter;
import cn.asany.website.landing.convert.PosterConverter;
import cn.asany.website.landing.convert.StoreConverter;
import cn.asany.website.landing.domain.LandingPage;
import cn.asany.website.landing.domain.LandingPoster;
import cn.asany.website.landing.domain.LandingStore;
import cn.asany.website.landing.graphql.input.*;
import cn.asany.website.landing.graphql.type.LandingPageConnection;
import cn.asany.website.landing.graphql.type.LandingPosterConnection;
import cn.asany.website.landing.graphql.type.LandingStoreConnection;
import cn.asany.website.landing.service.LandingService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.Optional;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
      LandingPageWhereInput where, int page, int pageSize, Sort orderBy) {
    where = ObjectUtil.defaultValue(where, new LandingPageWhereInput());
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    return Kit.connection(
        landingService.findLandingPagePager(pageable, where.toFilter()),
        LandingPageConnection.class);
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
      LandingPosterWhere where, int page, int pageSize, Sort orderBy) {
    where = ObjectUtil.defaultValue(where, new LandingPosterWhere());
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    return Kit.connection(
        landingService.findLandingPosterPage(pageable, where.toFilter()),
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
      LandingStoreWhereInput where, int page, int pageSize, Sort orderBy) {
    where = ObjectUtil.defaultValue(where, new LandingStoreWhereInput());
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    return Kit.connection(
        landingService.findLandingStorePage(pageable, where.toFilter()),
        LandingStoreConnection.class);
  }
}
