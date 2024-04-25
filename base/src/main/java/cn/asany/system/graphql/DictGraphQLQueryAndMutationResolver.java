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
package cn.asany.system.graphql;

import cn.asany.base.utils.Hashids;
import cn.asany.system.domain.Dict;
import cn.asany.system.domain.DictKey;
import cn.asany.system.domain.DictType;
import cn.asany.system.graphql.input.DictWhereInput;
import cn.asany.system.graphql.type.DictConnection;
import cn.asany.system.service.DictService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.error.ValidationException;
import net.asany.jfantasy.framework.util.common.StringUtil;
import net.asany.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class DictGraphQLQueryAndMutationResolver
    implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final DictService dictService;

  public DictGraphQLQueryAndMutationResolver(DictService dictService) {
    this.dictService = dictService;
  }

  /** 查询数据字典类型 */
  public List<DictType> dictTypes() {
    return dictService.getDictTypes();
  }

  /**
   * 数据字典分页查询
   *
   * @param where 过滤器
   * @param page 页码
   * @param pageSize 每页显示数据条数
   * @param orderBy 排序
   * @return DictConnection
   */
  public DictConnection dictsConnection(
      DictWhereInput where, int page, int pageSize, Sort orderBy) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    return Kit.connection(dictService.findPage(pageable, where.toFilter()), DictConnection.class);
  }

  /**
   * 数据字典
   *
   * @param where 过滤器
   * @return List<Dict>
   */
  public List<Dict> dicts(DictWhereInput where) {
    return dictService.findAll(where.toFilter());
  }

  /** 根据数据字典id，查询数据字典 */
  public Dict dict(String id, String code, String type, List<String> type_in) {
    if (StringUtil.isNotBlank(id)) {
      return dictService.get(Hashids.parseId(id));
    }
    if (StringUtil.isBlank(code)) {
      throw new ValidationException("非 ID 查询时, Code 不能为空");
    }
    if ((type_in == null || type_in.isEmpty()) && StringUtil.isBlank(type)) {
      throw new ValidationException("非 ID 查询时，必须同时提供 type_in 与 type 必须传入其中一个");
    }
    if (StringUtil.isNotBlank(type)) {
      return dictService.get(DictKey.newInstance(code, type));
    }
    PropertyFilter filter = PropertyFilter.newFilter();
    filter.in("type", type_in);
    filter.equal("code", code);
    return dictService.findOne(filter).orElse(null);
  }
}
