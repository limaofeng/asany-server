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
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * 职务
 *
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORG_JOB")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Job extends BaseBusEntity {

  private static final long serialVersionUID = -7020427994563623645L;

  @Id
  @Column(name = "ID", precision = 22)
  @TableGenerator
  private Long id;

  /** 职务名称 */
  @Column(name = "NAME", nullable = false, length = 50)
  private String name;

  /** 职务描述信息 */
  @Column(name = "DESCRIPTION", length = 250)
  private String description;

  /** 职务所属组织 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ORGANIZATION_ID",
      foreignKey = @ForeignKey(name = "FK_ORG_JOB_OID"),
      updatable = false,
      nullable = false)
  private Organization organization;

  /** 职务级别 */
  @Column(name = "level", length = 10)
  private Long level;
}
