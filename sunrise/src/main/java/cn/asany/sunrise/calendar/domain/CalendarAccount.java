package cn.asany.sunrise.calendar.domain;

import cn.asany.security.core.domain.User;
import cn.asany.sunrise.calendar.domain.enums.CalendarAccountType;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 日历
 *
 * @author limaofeng
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "SUNRISE_CALENDAR_ACCOUNT")
public class CalendarAccount extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 名称 */
  @Column(name = "NAME", length = 50, nullable = false)
  private String name;
  /** 描述 */
  @Column(name = "DESCRIPTION", length = 100)
  private String description;
  /** 类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 10)
  private CalendarAccountType type;
  /** 日历提供者 */
  @Column(name = "PROVIDER", length = 10)
  private String provider;
  /** 是否启用 */
  @Column(name = "ENABLED")
  private Boolean enabled;
  /** 排序字段 */
  @Column(name = "SORT")
  private Integer index;
  /** 所有者 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "OWNER_ID", foreignKey = @ForeignKey(name = "FK_CALENDAR_ACCOUNT_OWNER"))
  @ToString.Exclude
  private User owner;
}
