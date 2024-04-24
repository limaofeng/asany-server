package cn.asany.pm.issue.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.SoftDeletableBaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12 @Deprecated 任务关注
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PM_ISSUE_FOLLOW")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Follow extends SoftDeletableBaseBusEntity {
  /** ID */
  @Id
  @Column(name = "ID")
  @TableGenerator
  private Long id;

  /** 任务id */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ISSUE_ID", foreignKey = @ForeignKey(name = "FK_ISSUE_FOLLOW_ISSUE"))
  private Issue issue;

  /** 关注人 */
  @Column(name = "UID", length = 50)
  private Long uid;
}
