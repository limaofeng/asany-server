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
package cn.asany.system.graphql.input;

import cn.asany.base.utils.Hashids;
import cn.asany.system.domain.Dict;
import cn.asany.system.domain.DictKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.asany.jfantasy.graphql.inputs.WhereInput;

/**
 * 字典查询条件
 *
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
