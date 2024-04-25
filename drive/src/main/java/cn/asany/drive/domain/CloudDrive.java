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
package cn.asany.drive.domain;

import cn.asany.drive.domain.enums.CloudDriveType;
import cn.asany.storage.data.domain.Space;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.Hibernate;

// import org.hibernate.annotations.AnyMetaDef;

// import org.hibernate.annotations.MetaValue;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "CLOUD_DRIVE",
    uniqueConstraints =
        @UniqueConstraint(
            name = "UK_STORAGE_CLOUD_DRIVE",
            columnNames = {"OWNER_TYPE", "OWNER", "TYPE"}))
public class CloudDrive extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @TableGenerator
  private Long id;

  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 云盘类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20)
  private CloudDriveType type;

  /** 存储位置 */
  @ManyToOne
  @JoinColumn(
      name = "SPACE_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_CLOUD_DRIVE_SPACE"))
  private Space space;

  /** 存储空间 */
  @OneToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
  @PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "DRIVE_ID")
  @ToString.Exclude
  private CloudDriveQuota quota;

  /** 所有者类型 */
  @Column(name = "OWNER_TYPE", length = 25, nullable = false)
  private String ownerType;

  /** 所有者ID */
  @Column(name = "OWNER", nullable = false)
  private Long ownerId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    CloudDrive that = (CloudDrive) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
