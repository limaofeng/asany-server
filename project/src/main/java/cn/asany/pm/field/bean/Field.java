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

import cn.asany.pm.field.bean.enums.FieldCategory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
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
@Table(name = "ISSUE_FIELD")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Field extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @TableGenerator
  private Long id;

  @Column(name = "NAME", length = 50, nullable = false)
  private String name;

  @Column(name = "LABEL", length = 50, nullable = false)
  private String label;

  @Enumerated(EnumType.STRING)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "TYPE", foreignKey = @ForeignKey(name = "FK_ISSUE_FIELD_TYPE_ID"))
  @ToString.Exclude
  private FieldType type;

  @Enumerated(EnumType.STRING)
  @Column(name = "CATEGORY", length = 20, nullable = false)
  private FieldCategory category;

  @Column(name = "RENDERER", length = 20)
  private String renderer;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Field field = (Field) o;
    return id != null && Objects.equals(id, field.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
