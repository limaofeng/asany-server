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

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.StorageSpace;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Objects;
import java.util.Set;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.converter.StringSetConverter;
import org.hibernate.Hibernate;

/**
 * 文件上传时，为其指定的上传目录
 *
 * <p>通过Key获取上传的目录及文件管理器
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-7-23 上午10:30:06
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "STORAGE_SPACE")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class Space extends BaseBusEntity implements StorageSpace {

  private static final long serialVersionUID = 8150927437017643578L;

  @Id
  @Column(name = "ID", nullable = false, updatable = false, length = 50, unique = true)
  private String id;

  /** 空间名称 */
  @Column(name = "NAME", length = 250)
  private String name;

  /** 目录名称 */
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(
      name = "FOLDER_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_STORAGE_SPACE_FOLDER_ID"))
  @ToString.Exclude
  private FileDetail vFolder;

  /** 使用的插件 */
  @Convert(converter = StringSetConverter.class)
  @Column(name = "PLUGINS", length = 250)
  private Set<String> plugins;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Space space = (Space) o;
    return id != null && Objects.equals(id, space.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  @Transient
  public FileObject getFolder() {
    return this.vFolder.toFileObject(this);
  }
}
