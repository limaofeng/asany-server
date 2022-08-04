package cn.asany.pm.issue.core.domain;

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
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12 @Deprecated 任务时间追踪
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "PM_ISSUE_TIME_TRACK")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TimeTrack extends BaseBusEntity {
  /** ID */
  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 任务 */
  @OneToMany(
      mappedBy = "timeTrack",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  @ToString.Exclude
  private List<Issue> issues;
  /** 预计时长 */
  @Column(name = "ESTIMATED", length = 50)
  private Integer estimated;
  /** 剩余时长 */
  @Column(name = "REMAINING", length = 50)
  private Integer remaining;
  /** 记录时长 */
  @Column(name = "LOGGED", length = 50)
  private Long logged;
  /** 任务开始记录时间 */
  @Column(name = "TRACK_TIME", length = 50)
  private Date trackDate;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    TimeTrack timeTrack = (TimeTrack) o;
    return id != null && Objects.equals(id, timeTrack.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
