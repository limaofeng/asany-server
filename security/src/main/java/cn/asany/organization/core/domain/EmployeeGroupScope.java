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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;

/**
 * 用戶組分类
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORG_EMPLOYEE_GROUP_SCOPE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EmployeeGroupScope extends BaseBusEntity {

  @Id
  @Column(name = "ID", precision = 22)
  private String id;

  @Column(name = "NAME")
  private String name;

  /** 用户组范围 */
  @OneToMany(mappedBy = "scope", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private List<EmployeeGroup> groups;

  /** 所属组织 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ORGANIZATION_ID",
      foreignKey = @ForeignKey(name = "FK_GROUP_SCOPE_OID"),
      updatable = false,
      nullable = false)
  private Organization organization;
}
