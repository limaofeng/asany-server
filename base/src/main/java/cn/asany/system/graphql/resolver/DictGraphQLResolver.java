package cn.asany.system.graphql.resolver;

import cn.asany.base.utils.Hashids;
import cn.asany.system.domain.Dict;
import cn.asany.system.service.DictService;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.Collections;
import java.util.List;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
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
