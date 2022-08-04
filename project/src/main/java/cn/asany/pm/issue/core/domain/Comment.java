package cn.asany.pm.issue.core.domain;

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
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 任务评论
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
@Entity(name = "IssueComment")
@Table(name = "PM_ISSUE_COMMENT")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Comment extends BaseBusEntity {
  /** ID */
  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 任务id */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ISSUE_ID", foreignKey = @ForeignKey(name = "FK_ISSUE_ANNOTATE_ISSUE"))
  @ToString.Exclude
  private Issue issue;
  /** 用户ID */
  @Column(name = "UID", length = 50)
  private Long uid;
  /** 注释内容 */
  @Column(name = "CONTENT", length = 200)
  private String content;
  /** 注释时间 */
  @Column(name = "CONTENT_DATE", length = 200)
  private Date contentDate;

  @Convert(converter = FileObjectsConverter.class)
  @Column(name = "ATTACHMENTS", columnDefinition = "json")
  private List<FileObject> attachments;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Comment that = (Comment) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
