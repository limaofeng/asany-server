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
import org.hibernate.Hibernate;

/**
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
@Table(name = "ISSUE_FIELD_TYPE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FieldType extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, length = 24)
  private String id;

  /** 类型名称 */
  @Column(name = "NAME", length = 50, nullable = false)
  private String name;

  /** 描述 */
  @Column(name = "DESCRIPTION", length = 500, nullable = false)
  private String description;

  /** 示例值 */
  @Column(name = "EXAMPLE_VALUE", length = 500, nullable = false)
  private String example;

  /** 值类型 */
  @Column(name = "VALUE_TYPE", length = 20, nullable = false)
  private String valueType;

  /** 引用类型 */
  @Column(name = "REFERENCE_TYPE", length = 20, nullable = false)
  private String reference;

  /** 是否为集合类型 */
  @Column(name = "IS_LIST", length = 1, nullable = false)
  private boolean list;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    FieldType fieldType = (FieldType) o;
    return id != null && Objects.equals(id, fieldType.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
