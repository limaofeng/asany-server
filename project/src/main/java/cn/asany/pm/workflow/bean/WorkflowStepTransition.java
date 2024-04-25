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
package cn.asany.pm.workflow.bean;

import cn.asany.pm.screen.bean.IssueScreen;
import cn.asany.pm.screen.bean.screenbind.ScreenDeserializer;
import cn.asany.pm.screen.bean.screenbind.ScreenSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.Hibernate;

/**
 * 步骤转换
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
@Table(name = "ISSUE_WORKFLOW_STEP_TRANSITION")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class WorkflowStepTransition extends BaseBusEntity {

  @Id
  @Column(name = "ID", length = 22)
  @TableGenerator
  private Long id;

  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 描述 */
  @Column(name = "DESCRIPTION", length = 200)
  private String description;

  /** 目标步骤 */
  @JsonProperty("destination")
  @JoinColumn(
      name = "SID",
      foreignKey = @ForeignKey(name = "FK_GD_ISSUE_WORKFLOW_STEP_TRANSITION_SID"))
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private WorkflowStep destination;

  /** 界面 */
  @JsonProperty("view")
  @JsonSerialize(using = ScreenSerializer.class)
  @JsonDeserialize(using = ScreenDeserializer.class)
  @JoinColumn(
      name = "SCREEN_ID",
      foreignKey = @ForeignKey(name = "FK_GD_ISSUE_WORKFLOW_STEP_TRANSITION_SCREEN_ID"))
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private IssueScreen view;

  /** 该步骤 */
  @JsonProperty("step")
  @JoinColumn(
      name = "STEP_ID",
      foreignKey = @ForeignKey(name = "FK_GD_ISSUE_WORKFLOW_STEP_TRANSITION_STEP_ID"))
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private WorkflowStep step;

  @OneToMany(
      mappedBy = "transition",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  @ToString.Exclude
  private List<WorkflowSchedule> schedules;

  @OneToMany(
      mappedBy = "transition",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  @ToString.Exclude
  private List<WorkflowStepTransitionCondition> conditions;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    WorkflowStepTransition that = (WorkflowStepTransition) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
