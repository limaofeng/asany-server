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
package cn.asany.cms.circle.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;

/**
 * 圈子
 *
 * @author admin
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "CMS_CIRCLE_MEMBER",
    uniqueConstraints =
        @UniqueConstraint(
            name = "UK_CIRCLE_MEMBER",
            columnNames = {"CIRCLE_ID", "USER_ID"}))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CircleMember extends BaseBusEntity {
  private static final long serialVersionUID = 5943657998408920023L;

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "circle_gen")
  @TableGenerator(
      name = "circle_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "cms_circle:id",
      valueColumnName = "gen_value")
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "CIRCLE_ID", nullable = false)
  private Long category;

  @Column(name = "USER_ID", length = 32)
  private Long userId;
}
