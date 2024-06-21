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
package cn.asany.system.graphql.resolver;

import cn.asany.base.utils.Hashids;
import cn.asany.system.domain.Dict;
import cn.asany.system.service.DictService;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.Collections;
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.framework.util.common.StringUtil;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class DictGraphQLResolver implements GraphQLResolver<Dict> {

  private final DictService dictService;

  public DictGraphQLResolver(DictService dictService) {
    this.dictService = dictService;
  }

  public String id(Dict dict) {
    return Hashids.toId(dict.getId().toString());
  }

  public List<Dict> children(Dict dict) {
    return ObjectUtil.defaultValue(dict.getChildren(), Collections::emptyList);
  }

  public List<Dict> parents(Dict dict) {
    String[] codes = StringUtil.tokenizeToStringArray(dict.getPath(), Dict.PATH_SEPARATOR);
    return dictService.findAll(
        PropertyFilter.newFilter().in("code", codes), Sort.by("index").ascending());
  }
}
