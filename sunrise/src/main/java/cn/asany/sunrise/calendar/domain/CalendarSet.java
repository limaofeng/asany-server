package cn.asany.sunrise.calendar.domain;

import cn.asany.security.core.domain.User;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 日历集
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
@Table(name = "SUNRISE_CALENDAR_SET")
public class CalendarSet extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;
  /** 排序字段 */
  @Column(name = "SORT")
  private Integer index;
  /** 默认日历 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "DEFAULT_CALENDAR",
      foreignKey = @ForeignKey(name = "FK_CALENDAR_SET_DEFAULT_CALENDAR"))
  @ToString.Exclude
  private Calendar defaultCalendar;
  /** 日历 */
  @ToString.Exclude
  @ManyToMany(targetEntity = Calendar.class, fetch = FetchType.LAZY)
  @JoinTable(
      name = "SUNRISE_CALENDAR_SET_ITEMS",
      joinColumns =
          @JoinColumn(
              name = "SET_ID",
              foreignKey = @ForeignKey(name = "FK_SUNRISE_CALENDAR_SET_SID")),
      inverseJoinColumns =
          @JoinColumn(
              name = "CALENDAR_ID",
              foreignKey = @ForeignKey(name = "FK_SUNRISE_CALENDAR_SET_CID")))
  private List<Calendar> calendars;
  /** 所有者 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "OWNER_ID", foreignKey = @ForeignKey(name = "FK_CALENDAR_SET_OWNER_ID"))
  @ToString.Exclude
  private User owner;
}
