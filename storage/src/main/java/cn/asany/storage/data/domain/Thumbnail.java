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
package cn.asany.storage.data.domain;

import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(
    name = "STORAGE_THUMBNAIL",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "UK_STORAGE_THUMBNAIL_SIZE",
          columnNames = {"SOURCE_ID", "SIZE"})
    })
@AllArgsConstructor
@Builder
public class Thumbnail extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @TableGenerator
  private Long id;

  /** 比例 */
  @Column(name = "SIZE", length = 100, nullable = false)
  private String size;

  /** 原文件 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "SOURCE_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_STORAGE_THUMBNAIL_SOURCE_ID"))
  @ToString.Exclude
  private FileDetail source;

  /** 缩略图 */
  @OneToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  @JoinColumn(
      name = "FILE_ID",
      referencedColumnName = "ID",
      foreignKey = @ForeignKey(name = "FK_STORAGE_THUMBNAIL_FILE_ID"))
  @ToString.Exclude
  private FileDetail file;
}
