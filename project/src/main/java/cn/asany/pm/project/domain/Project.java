package cn.asany.pm.project.domain;

import cn.asany.storage.api.FileObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 项目
 *
 * @author limaofeng
 * @date 2019-05-24 09:34
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "PM_PROJECT")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Project extends BaseBusEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 标志 */
  @Column(name = "LOGO", length = 500, columnDefinition = "JSON")
  @Type(type = "file")
  private FileObject logo;
  /** 项目名称 */
  @Column(name = "NAME", length = 20)
  private String name;
  /** 描述 */
  @Column(name = "DESCRIPTION", length = 500)
  private String description;

  /** 阶段 */
  //    @Enumerated(EnumType.STRING)
  //    @Column(name = "CATEGORY", length = 20)
  //    private IssueProjectCategory category;
  /** 工作流方案 */
  //    @ManyToOne(fetch = FetchType.LAZY)
  //    @JoinColumn(name = "WORKFLOW_SCHEME", foreignKey = @ForeignKey(name =
  // "FK_ISSUE_PROJECT_WORKFLOW_SCHEME"))
  //    private IssueWorkflowScheme workflowScheme;
  /** 问题类型方案 */
  //    @ManyToOne(fetch = FetchType.LAZY)
  //    @JoinColumn(name = "TYPE_SCHEME", foreignKey = @ForeignKey(name =
  // "FK_ISSUE_PROJECT_TYPE_SCHEME"))
  //    private IssueTypeScheme issueTypeScheme;
  /** 字段配置方案 */
  //    @ManyToOne(fetch = FetchType.LAZY)
  //    @JoinColumn(name = "FIELD_CONFIGURATION_SCHEME", foreignKey = @ForeignKey(name =
  // "FK_ISSUE_PROJECT_FIELD_CONFIGURATION_SCHEME"))
  //    private IssueFieldConfigurationScheme fieldConfigurationScheme;
  /** 优先级方案 */
  //    @ManyToOne(fetch = FetchType.LAZY)
  //    @JoinColumn(name = "PRIORITY_SCHEME", foreignKey = @ForeignKey(name =
  // "FK_ISSUE_PROJECT_PRIORITY_SCHEME"))
  //    private PriorityScheme priorityScheme;
  /** 任务列表 */
  //    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE,
  // CascadeType.PERSIST})
  //    private List<Issue> issues;
  /** 方案列表 */
  //    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE,
  // CascadeType.PERSIST})
  //    private List<IssueResolution> issueResolutions;

  //    @ManyToOne(fetch = FetchType.LAZY)
  //    @JoinColumn(name = "PERMISSION_SCHEME", foreignKey = @ForeignKey(name =
  // "FK_ISSUE_PROJECT_PERMISSION_SCHEME"))
  //    private PermissionScheme permissionScheme;

  //    /**
  //     * 自动派单规则方案
  //     */
  //    @ManyToOne(fetch = FetchType.LAZY)
  //    @JoinColumn(name = "RULE_SCHEME", foreignKey = @ForeignKey(name = "FK_ISSUE_RULE_SCHEME"))
  //    private IssueRuleScheme issueRuleScheme;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Project project = (Project) o;
    return id != null && Objects.equals(id, project.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
