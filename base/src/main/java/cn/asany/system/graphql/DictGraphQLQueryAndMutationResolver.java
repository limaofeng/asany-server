package cn.asany.system.graphql;

import cn.asany.base.utils.Hashids;
import cn.asany.system.bean.Dict;
import cn.asany.system.bean.DictKey;
import cn.asany.system.bean.DictType;
import cn.asany.system.graphql.input.DictFilter;
import cn.asany.system.graphql.type.DictConnection;
import cn.asany.system.service.DictService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.graphql.util.Kit;
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
   * @param filter 过滤器
   * @param page 页码
   * @param pageSize 每页显示数据条数
   * @param orderBy 排序
   * @return DictConnection
   */
  public DictConnection dictsConnection(
      DictFilter filter, int page, int pageSize, OrderBy orderBy) {
    Pager<Dict> pager = Pager.newPager(page, pageSize, orderBy);
    return Kit.connection(dictService.findPager(pager, filter.build()), DictConnection.class);
  }

  /**
   * 数据字典
   *
   * @param filter 过滤器
   * @return List<Dict>
   */
  public List<Dict> dicts(DictFilter filter) {
    return dictService.findAll(filter.build());
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
    PropertyFilterBuilder builder = PropertyFilter.builder();
    builder.in("type", type_in);
    builder.equal("code", code);
    return dictService.findOne(builder.build()).orElse(null);
  }
}
