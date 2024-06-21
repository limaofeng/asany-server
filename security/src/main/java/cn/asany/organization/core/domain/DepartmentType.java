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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * 部门类型
 *
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORG_DEPARTMENT_TYPE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DepartmentType extends BaseBusEntity {
  @Id
  @Column(name = "ID", precision = 22)
  @TableGenerator
  private Long id;

  /** 编码 */
  @Column(name = "CODE", length = 20)
  private String code;

  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 组织纬度 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "DIMENSION_ID",
      foreignKey = @ForeignKey(name = "FK_DEPARTMENT_TYPE_ORGANIZATION_DIMENSION"),
      updatable = false,
      nullable = false)
  private OrganizationDimension dimension;

  /** 组织 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonBackReference
  @JoinColumn(
      name = "ORGANIZATION_ID",
      foreignKey = @ForeignKey(name = "FK_ORG_DEPARTMENT_TYPE_OID"))
  private Organization organization;

  /** 是否支持多部门 */
  @Column(name = "MULTI_SECTORAL")
  private Boolean multiSectoral;

  /** 最大兼岗人数 */
  @Column(name = "AND_POST")
  private Long andPost;

  public Boolean getMultiSectoral() {
    return multiSectoral;
  }
}
