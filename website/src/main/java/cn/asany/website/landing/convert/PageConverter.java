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
package cn.asany.website.landing.convert;

import cn.asany.website.landing.domain.LandingPage;
import cn.asany.website.landing.domain.LandingPoster;
import cn.asany.website.landing.domain.LandingStore;
import cn.asany.website.landing.graphql.input.LandingPageCreateInput;
import cn.asany.website.landing.graphql.input.LandingPageUpdateInput;
import cn.asany.website.landing.service.LandingService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.asany.jfantasy.framework.spring.SpringBeanUtils;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PageConverter {

  @Mappings({
    @Mapping(source = "poster", target = "poster", qualifiedByName = "toPosterId"),
    @Mapping(source = "stores", target = "stores", qualifiedByName = "toStoresFromIds"),
  })
  LandingPage toPage(LandingPageCreateInput input);

  @Mappings({
    @Mapping(source = "poster", target = "poster", qualifiedByName = "toPosterId"),
    @Mapping(source = "stores", target = "stores", qualifiedByName = "toStoresFromIds"),
  })
  LandingPage toPage(LandingPageUpdateInput input);

  @Named("toPosterId")
  default LandingPoster toPosterId(Long id) {
    if (id == null) {
      return null;
    }
    LandingService landingService = SpringBeanUtils.getBean(LandingService.class);
    return landingService.findPoster(id).orElse(null);
  }

  @Named("toStoresFromIds")
  default List<LandingStore> toStoresFromIds(List<Long> ids) {
    if (ids == null || ids.isEmpty()) {
      return new ArrayList<>();
    }
    LandingService landingService = SpringBeanUtils.getBean(LandingService.class);
    return ids.stream()
        .map(landingService::findStore)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
  }
}
