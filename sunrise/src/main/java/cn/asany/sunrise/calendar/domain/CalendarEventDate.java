package cn.asany.sunrise.calendar.domain;

import jakarta.persistence.*;
import java.util.Date;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * 事件日期 <br>
 * 如果跨天事件，每天一条记录
 *
 * @author limaofeng
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "SUNRISE_CALENDAR_EVENT_DATE")
public class CalendarEventDate extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @TableGenerator
  private Long id;

  /** 日期 */
  @Temporal(TemporalType.DATE)
  @Column(updatable = false, name = "DATE")
  private Date date;

  /** 事件 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "EVENT_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_CALENDAR_EVENT_DATE_EVENT_ID"))
  @ToString.Exclude
  private CalendarEvent event;
}
