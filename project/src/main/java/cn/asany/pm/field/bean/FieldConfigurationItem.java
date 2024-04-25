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
package cn.asany.pm.field.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.Hibernate;

/**
 * 字段项配置
 *
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "ISSUE_FIELD_CONFIGURATION_ITEM")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FieldConfigurationItem extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @TableGenerator
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "FIELD_ID",
      foreignKey = @ForeignKey(name = "FK_FIELD_CONFIGURATION_ITEM_FIELD_ID"))
  @ToString.Exclude
  private Field field;

  @Column(name = "REQUIRED")
  private Boolean required;

  @Column(name = "RENDERER", length = 32)
  private String renderer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "CONFIG_ID",
      foreignKey = @ForeignKey(name = "FK_FIELD_CONFIGURATION_ITEM_CONFIG_ID"))
  @ToString.Exclude
  private FieldConfiguration fieldConfiguration;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    FieldConfigurationItem that = (FieldConfigurationItem) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
