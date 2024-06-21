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
package cn.asany.base.common.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Owner<T extends Enum<T>> {
  /** 所有者ID */
  @Column(name = "OWNER_ID", length = 30)
  private String id;

  /** 所有者类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "OWNER_TYPE", length = 20)
  private T type;

  /** 所有者名称 */
  @Column(name = "OWNER_NAME", length = 50)
  private String name;
}
