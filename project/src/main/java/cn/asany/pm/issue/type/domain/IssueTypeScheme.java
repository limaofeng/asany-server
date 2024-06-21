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
package cn.asany.pm.issue.type.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.Hibernate;

/**
 * 问题类型方案
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "PM_ISSUE_TYPE_SCHEME")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class IssueTypeScheme extends BaseBusEntity {

  /** 主键ID */
  @Id
  @Column(name = "ID", length = 22)
  @TableGenerator
  private Long id;

  /** 方案名称 */
  @Column(name = "NAME", length = 100)
  private String name;

  /** 方案描述 */
  @Column(name = "DESCRIPTION", length = 200)
  private String description;

  /** 默认问题类型 */
  @JoinColumn(
      name = "DEFAULT_ISSUE_TYPE",
      foreignKey = @ForeignKey(name = "FK_GD_TYPE_SCHEME_DEFAULT_ISSUE_TYPE"))
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private IssueType defaultType;

  /** 该问题类型方案有哪些问题类型 */
  @ManyToMany(targetEntity = IssueType.class, fetch = FetchType.LAZY)
  @JoinTable(
      name = "PM_ISSUE_TYPE_SCHEME_ITEM",
      joinColumns =
          @JoinColumn(
              name = "SCHEME_ID",
              foreignKey = @ForeignKey(name = "FK_ISSUE_TYPE_SCHEME_ITEM_SID")),
      inverseJoinColumns = @JoinColumn(name = "TYPE_ID"),
      foreignKey = @ForeignKey(name = "FK_ISSUE_TYPE_SCHEME_ITEM_TID"))
  @ToString.Exclude
  private List<IssueType> types;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    IssueTypeScheme that = (IssueTypeScheme) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
