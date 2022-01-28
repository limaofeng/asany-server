package cn.asany.sunrise.calendar.bean;

import cn.asany.security.core.bean.User;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 日历偏好设置
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
@Table(name = "SUNRISE_CALENDAR_PREFERENCE")
public class CalendarPreference extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  /** 默认日历 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "DEFAULT_CALENDAR",
      foreignKey = @ForeignKey(name = "FK_CALENDAR_PREFERENCE_DEFAULT_CALENDAR"))
  @ToString.Exclude
  private Calendar defaultCalendar;

  /** 所有者 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "OWNER_ID", foreignKey = @ForeignKey(name = "FK_CALENDAR_PREFERENCE_OWNER_ID"))
  @ToString.Exclude
  private User owner;
}
