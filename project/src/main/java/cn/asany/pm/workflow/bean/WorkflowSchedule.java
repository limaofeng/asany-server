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

import cn.asany.pm.issue.core.domain.Issue;
import cn.asany.pm.project.domain.Project;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.Hibernate;

/**
 * 保存每一步操作的数据
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
@Table(name = "WORKFLOW_SCHEDULE")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class WorkflowSchedule extends BaseBusEntity {

  /** 主键ID */
  @Id
  @Column(name = "ID", length = 22)
  @TableGenerator
  private Long id;

  /** 问题的id */
  @JsonProperty("issue")
  @JoinColumn(name = "issue", foreignKey = @ForeignKey(name = "FK_WORKFLOW_SCHEDULE_ISSUE"))
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private Issue issue;

  /** 操作id */
  @JsonProperty("tran")
  @JoinColumn(name = "tran", foreignKey = @ForeignKey(name = "FK_WORKFLOW_SCHEDULE_STEP_TRAN"))
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private WorkflowStepTransition transition;

  /** 项目id */
  @JsonProperty("project")
  @JoinColumn(name = "project", foreignKey = @ForeignKey(name = "FK_WORKFLOW_SCHEDULE_PROJECT"))
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private Project project;

  /** 经办人 */
  @Column(name = "ASSIGNEE", length = 100)
  private Long assignee;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    WorkflowSchedule that = (WorkflowSchedule) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
