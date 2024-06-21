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
package cn.asany.nuwa.app.domain;

import cn.asany.nuwa.app.domain.enums.LicenceStatus;
import cn.asany.nuwa.app.domain.enums.LicenceType;
import cn.asany.organization.core.domain.Organization;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.Hibernate;

/**
 * 应用许可
 *
 * @author limaofeng
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "NUWA_APPLICATION_LICENCE",
    uniqueConstraints = {@UniqueConstraint(name = "UK_LICENCE_KEY", columnNames = "LICENCE_KEY")})
public class Licence extends BaseBusEntity {
  @Id
  @Column(name = "ID")
  @TableGenerator
  private Long id;

  /** 许可证 Key */
  @Column(name = "LICENCE_KEY", length = 1024)
  private String key;

  /** 许可证类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 50)
  private LicenceType type;

  /** 许可证状态 */
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", length = 50)
  private LicenceStatus status;

  /** 应用 */
  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "APPLICATION_ID",
      foreignKey = @ForeignKey(name = "FK_APPLICATION_LICENCE_APP"))
  private Application application;

  /** 持有人 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "OWNERSHIP",
      foreignKey = @ForeignKey(name = "FK_APPLICATION_LICENCE_OWNERSHIP"))
  private Organization ownership;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Licence licence = (Licence) o;
    return id != null && Objects.equals(id, licence.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
