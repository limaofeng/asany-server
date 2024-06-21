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

import cn.asany.pm.issue.type.domain.IssueType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.Hibernate;

/**
 * 给工作流选择问题类型
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
@Table(name = "ISSUE_WORKFLOW_STEP_ITEM")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class WorkflowAndIssueType extends BaseBusEntity {

  /** 主键ID */
  @Id
  @Column(name = "ID", length = 22)
  @TableGenerator
  private Long id;

  /** 工作流 */
  @JsonProperty("workflow")
  @JoinColumn(name = "FID", foreignKey = @ForeignKey(name = "FK_ISSUE_WORKFLOW_STEP_ITEM_WID"))
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private Workflow workflow;

  /** 任务类型 */
  @ManyToMany(
      targetEntity = IssueType.class,
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  @JoinTable(
      name = "PM_ISSUE_WORKFLOW_STEP_ITEM_ISSUE_TYPE",
      joinColumns =
          @JoinColumn(
              name = "ITEM_ID",
              foreignKey = @ForeignKey(name = "FK_ISSUE_WORKFLOW_STEP_ITEM_ISSUE_TYPE_WID")),
      inverseJoinColumns = @JoinColumn(name = "TYPE_ID"),
      foreignKey = @ForeignKey(name = "FK_ISSUE_WORKFLOW_STEP_ITEM_ISSUE_TYPE_TID"))
  @ToString.Exclude
  private List<IssueType> issueTypes;

  /** 工作流方案 */
  @JsonProperty("scheme")
  @JoinColumn(name = "SID", foreignKey = @ForeignKey(name = "FK_ISSUE_WORKFLOW_STEP_ITEM_SID"))
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private WorkflowScheme workflowScheme;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    WorkflowAndIssueType that = (WorkflowAndIssueType) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
