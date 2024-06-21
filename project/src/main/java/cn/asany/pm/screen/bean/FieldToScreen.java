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
package cn.asany.pm.screen.bean;

import cn.asany.pm.field.bean.FieldConfigurationItem;
import cn.asany.pm.screen.bean.screenbind.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.Hibernate;

/**
 * 将字段分配给页面
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
@Table(name = "FIELD_SCREEN")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class FieldToScreen extends BaseBusEntity {

  @Id
  @Column(name = "ID", length = 22)
  @TableGenerator
  private Long id;

  /** 页面 */
  @JsonProperty("screen")
  @JsonSerialize(using = ScreenSerializer.class)
  @JsonDeserialize(using = ScreenDeserializer.class)
  @JoinColumn(name = "SID", foreignKey = @ForeignKey(name = "FK_GD_FIELD_SCREEN_SID"))
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private IssueScreen screen;

  /** 字段 */
  @JsonProperty("field")
  @JsonSerialize(using = IssueFieldSerializer.class)
  @JsonDeserialize(using = IssueFieldDeserializer.class)
  @JoinColumn(name = "FID", foreignKey = @ForeignKey(name = "FK_GD_FIELD_SCREEN_FID"))
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private FieldConfigurationItem field;

  /** TabPane */
  @JsonProperty("tabPane")
  @JsonSerialize(using = DomainSerializer.class)
  @JsonDeserialize(using = DomainDeserializer.class)
  @JoinColumn(name = "TID", foreignKey = @ForeignKey(name = "FK_GD_FIDEL_TAB_PANE_TID"))
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private IssueScreenTabPane tabPane;

  /** 排序字段 */
  @Column(name = "SORT", length = 22)
  private Long order;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    FieldToScreen that = (FieldToScreen) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
