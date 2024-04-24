package cn.asany.pm.issue.core.domain;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.converter.FileObjectsConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.Hibernate;

/**
 * 任务日志
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
@Table(name = "PM_ISSUE_WORKLOG")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Worklog extends BaseBusEntity {
  /** ID */
  @Id
  @Column(name = "ID", updatable = false, nullable = false)
  @TableGenerator
  private Long id;

  /** 任务id */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ISSUE_ID", foreignKey = @ForeignKey(name = "FK_ISSUE_LOG_ISSUE"))
  @ToString.Exclude
  private Issue issue;

  /** 日志内容 */
  @Column(name = "CONTENT", length = 50)
  private String content;

  /** 填写时间 */
  @Column(name = "LOG_TIME", length = 50)
  private Date logTime;

  /** 用户ID */
  @Column(name = "UID", length = 50)
  private Long uid;

  @Convert(converter = FileObjectsConverter.class)
  @Column(name = "ATTACHMENTS", columnDefinition = "json")
  private List<FileObject> attachments;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Worklog worklog = (Worklog) o;
    return id != null && Objects.equals(id, worklog.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
