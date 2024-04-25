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

import cn.asany.pm.workflow.bean.WorkflowStepTransition;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * 页面
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ISSUE_SCREEN")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class IssueScreen extends BaseBusEntity {

  /** 主键ID */
  @Id
  @Column(name = "ID", length = 22)
  @TableGenerator
  private Long id;

  /** 页面名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 页面标题 */
  @Column(name = "TITLE", length = 50)
  private String title;

  /** 页面描述 */
  @Column(name = "DESCRIPTION", length = 50)
  private String description;

  /** 该页面包含的 TabPane */
  @OneToMany(
      mappedBy = "issueScreen",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  private List<IssueScreenTabPane> tabs;

  /** 该页面包含的字段 */
  @OneToMany(
      mappedBy = "screen",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  private List<FieldToScreen> fields;

  /** 该页面包属于哪一个步骤 */
  @OneToMany(
      mappedBy = "view",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  private List<WorkflowStepTransition> workflowStepTransitions;
}
