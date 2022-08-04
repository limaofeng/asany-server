package cn.asany.pm.screen.bean;

import cn.asany.pm.workflow.bean.WorkflowStepTransition;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

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
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
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
