package cn.asany.pm.issue.core.domain;

import cn.asany.pm.issue.attribute.domain.Resolution;
import cn.asany.pm.issue.attribute.domain.Status;
import cn.asany.pm.issue.priority.domain.Priority;
import cn.asany.pm.issue.type.domain.IssueType;
import cn.asany.pm.project.domain.Project;
import cn.asany.pm.project.domain.ProjectMember;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.converter.FileObjectsConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.SoftDeletableBaseBusEntity;

/**
 * 任务主表
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "PM_ISSUE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Issue extends SoftDeletableBaseBusEntity {
  /** 任务ID */
  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 任务类型 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "TYPE_ID", foreignKey = @ForeignKey(name = "FK_ISSUE_TYPE_ISSUE"))
  @ToString.Exclude
  private IssueType type;
  /** 名称 */
  @Column(name = "NAME", length = 100)
  private String name;
  /** 描述 */
  @Column(name = "DESCRIPTION", length = 500)
  private String description;
  /** 进度 */
  @Column(name = "PROGRESS", length = 10)
  private Integer progress;
  /** 发起时间 */
  @Column(name = "START_TIME")
  private Date startTime;
  /** 截止日期 */
  @Column(name = "END_TIME")
  private Date endTime;
  /** 发起人 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "REPORTER")
  @ToString.Exclude
  private ProjectMember reporter;
  /** 经办人 */
  @Column(name = "ASSIGNEE", length = 100)
  private Long assignee;
  /** 任务优先级 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PRIORITY_ID", foreignKey = @ForeignKey(name = "FK_ISSUE_PRIORITY_ISSUE"))
  @ToString.Exclude
  private Priority priority;
  /** 解决结果 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "RESOLUTION_ID", foreignKey = @ForeignKey(name = "FK_ISSUE_RESOLUTION_ISSUE"))
  @ToString.Exclude
  private Resolution resolution;
  /** 当前状态 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "STATUS_ID", foreignKey = @ForeignKey(name = "FK_ISSUE_STATUS_ISSUE"))
  @ToString.Exclude
  private Status status;
  /** 截止时间 */
  @Column(name = "DUE_DATE")
  private Date dueDate;
  /** 项目 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "PROJECT_ID",
      foreignKey = @ForeignKey(name = "FK_ISSUE_PROJECT"),
      nullable = false)
  @ToString.Exclude
  private Project project;
  /** 附件 */
  @Convert(converter = FileObjectsConverter.class)
  @Column(name = "ATTACHMENTS", columnDefinition = "json")
  private List<FileObject> attachments;
  /** 关注人列表 */
  @OneToMany(
      mappedBy = "issue",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
  @ToString.Exclude
  private List<Follow> follows;
  /** 任务注释列表 */
  @OneToMany(
      mappedBy = "issue",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
  @ToString.Exclude
  private List<Comment> comments;
  /** 任务日志列表 */
  @OneToMany(
      mappedBy = "issue",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
  @ToString.Exclude
  private List<Worklog> worklogs;
  /** 任务实际跟踪列表 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "TIME_TRACK_ID", foreignKey = @ForeignKey(name = "FK_ISSUE_TIME_TRACK_ISSUE"))
  @ToString.Exclude
  private TimeTrack timeTrack;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Issue issue = (Issue) o;
    return id != null && Objects.equals(id, issue.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
