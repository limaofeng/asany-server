package cn.asany.sunrise.calendar.bean;

import java.util.Date;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

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
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 日期 */
  @Temporal(TemporalType.DATE)
  @Column(updatable = false, name = "DATE")
  private Date date;
  /** 事件 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "EVENT_ID")
  @ToString.Exclude
  private CalendarEvent event;
}
