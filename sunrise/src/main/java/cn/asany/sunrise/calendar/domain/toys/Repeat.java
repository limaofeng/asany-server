package cn.asany.sunrise.calendar.domain.toys;

import cn.asany.sunrise.calendar.domain.enums.RepeatEnd;
import cn.asany.sunrise.calendar.domain.enums.RepeatType;
import java.util.Date;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 重复设置
 *
 * @author limaofeng
 */
@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Repeat {
  /** 重复设置 */
  @Enumerated(EnumType.STRING)
  @Column(name = "`REPEAT`", length = 10)
  private RepeatType repeat;
  /** 重复结束 */
  @Column(name = "REPEAT_END", length = 10)
  private RepeatEnd repeatEnd;
  /** 循环次数 */
  @Column(name = "REPEAT_END_TIMES")
  private Integer times;
  /** 时间 */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "REPEAT_END_ON_DATE")
  private Date onDate;
}
