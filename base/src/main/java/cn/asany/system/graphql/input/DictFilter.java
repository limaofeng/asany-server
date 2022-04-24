package cn.asany.system.graphql.input;

import cn.asany.base.utils.Hashids;
import cn.asany.system.bean.Dict;
import cn.asany.system.bean.DictKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jfantasy.graphql.inputs.QueryFilter;

public class DictFilter extends QueryFilter<DictFilter, Dict> {

  @JsonProperty("typePath_startsWith")
  public void setTypePath_startsWith(String path) {
    this.builder.startsWith("dictType.path", path);
  }

  @JsonProperty("parent")
  public void setParent(String parentKey) {
    DictKey dictKey = DictKey.newInstance(Hashids.parseId(parentKey));
    this.builder.equal("parent.code", dictKey.getCode()).equal("parent.type", dictKey.getType());
  }
}
