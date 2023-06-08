package cn.asany.system.graphql.input;

import cn.asany.base.utils.Hashids;
import cn.asany.system.domain.Dict;
import cn.asany.system.domain.DictKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jfantasy.graphql.inputs.WhereInput;

/**
 * 字典查询条件
 * @author limaofeng
 */
public class DictWhereInput extends WhereInput<DictWhereInput, Dict> {

  @JsonProperty("typePath_startsWith")
  public void setTypePath_startsWith(String path) {
    this.filter.startsWith("dictType.path", path);
  }

  @JsonProperty("parent")
  public void setParent(String parentKey) {
    DictKey dictKey = DictKey.newInstance(Hashids.parseId(parentKey));
    this.filter.equal("parent.code", dictKey.getCode()).equal("parent.type", dictKey.getType());
  }
}
