package cn.asany.sunrise.calendar.domain;

import cn.asany.security.core.domain.User;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

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
  @TableGenerator
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
