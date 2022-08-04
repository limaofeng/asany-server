package cn.asany.workflow.screen.domain;

import cn.asany.workflow.core.domain.WorkflowStepTransition;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 页面(这里用一句话描述这个类的作用)
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "FSM_SCREEN")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Screen extends BaseBusEntity {

  /** 主键ID */
  @Id
  @Column(name = "ID", length = 22)
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
  private List<ScreenTabPane> tabs;

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
  private List<WorkflowStepTransition> issueWorkflowStepTransitions;
}
