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
package cn.asany.pim.core.graphql.type;

import cn.asany.pim.core.domain.Device;
import java.util.List;
import lombok.*;
import net.asany.jfantasy.graphql.Edge;
import net.asany.jfantasy.graphql.types.BaseConnection;

/**
 * 角色查询接口
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceConnection extends BaseConnection<DeviceConnection.DeviceEdge, Device> {

  private List<DeviceEdge> edges;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class DeviceEdge implements Edge<Device> {
    private String cursor;
    private Device node;
  }
}
