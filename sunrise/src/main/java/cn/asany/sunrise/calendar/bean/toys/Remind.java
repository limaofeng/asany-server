package cn.asany.sunrise.calendar.bean.toys;

import cn.asany.sunrise.calendar.bean.enums.Alert;
import java.util.Date;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 提醒设置
 *
 * @author limaofeng
 */
@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Remind {
  /** 提醒设置 */
  @Enumerated(EnumType.STRING)
  @Column(name = "ALERT", length = 10)
  private Alert alert;
  /** 几 分钟/小时/天 */
  @Column(name = "ALERT_TIMES")
  private Integer times;
  /** 时间 */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "ALERT_ON_DATE")
  private Date onDate;
}
