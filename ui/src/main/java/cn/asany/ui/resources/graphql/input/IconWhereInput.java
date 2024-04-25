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
package cn.asany.ui.resources.graphql.input;

import cn.asany.ui.library.domain.Oplog;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.graphql.inputs.WhereInput;

/**
 * 图标查询过滤
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class IconWhereInput extends WhereInput<IconWhereInput, Oplog> {

  @JsonProperty("id_in")
  public void setIds(List<Long> ids) {
    this.filter.in("resourceId", ids);
  }

  public void setLibrary(Long library) {
    this.filter.equal("library.id", library);
  }

  public void setLibrary_in(List<Long> ids) {
    this.filter.in("library.id", ids);
  }
}
