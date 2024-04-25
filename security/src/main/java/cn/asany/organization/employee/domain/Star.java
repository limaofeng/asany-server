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
package cn.asany.organization.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * 为 收藏 / 点赞 等功能提供统一支持
 *
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "ORG_STAR",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "UK_ORG_STAR",
          columnNames = {"TYPE", "VALUE", "EMPLOYEE_ID"})
    })
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Star extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, length = 20)
  @TableGenerator
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "TYPE",
      updatable = false,
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_STAR_TYPE"))
  private StarType starType;

  @Column(name = "VALUE", updatable = false, nullable = false, length = 50)
  private String galaxy;

  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REFRESH})
  @JoinColumn(
      name = "EMPLOYEE_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_STAR_EMPLOYEE"))
  private Employee stargazer;

  /** 阅读时长 */
  @Column(name = "READING_TIME")
  private Long readingTime;
}
