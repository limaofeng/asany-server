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
package cn.asany.organization.core.domain;

import cn.asany.organization.core.domain.databind.DepartmentDeserializer;
import cn.asany.organization.core.domain.databind.DepartmentSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * @author limaofeng
 * @version V1.0 @Description: 部门
 * @date 2022/7/28 9:12 9:12
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORG_DEPARTMENT_ATTRIBUTE")
@JsonIgnoreProperties({
  "hibernateLazyInitializer",
  "handler",
  "children",
  "employees",
  "positions",
  "roles",
  "links",
  "grants"
})
public class DepartmentAttribute extends BaseBusEntity {

  @Id
  @Column(name = "ID", precision = 22)
  @TableGenerator
  private Long id;

  /** 部门 */
  @JsonSerialize(using = DepartmentSerializer.class)
  @JsonDeserialize(using = DepartmentDeserializer.class)
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
  @JoinColumn(
      name = "department_id",
      foreignKey = @ForeignKey(name = "FK_AUTH_DEPARTMENT_ATTRIBUTE_ID"),
      nullable = false,
      updatable = false)
  private Department department;

  /** 属性名称 */
  @Column(name = "attribute_name", length = 50)
  private String attributeName;

  /** 属性值 */
  @Column(name = "attribute_value", length = 50)
  private String attributeValue;

  /** 描述信息 */
  @Column(name = "DESCRIPTION", length = 150)
  private String description;
}
