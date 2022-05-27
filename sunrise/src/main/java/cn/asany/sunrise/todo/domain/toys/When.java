package cn.asany.sunrise.todo.domain.toys;

import cn.asany.sunrise.todo.domain.enums.WhenType;
import java.util.Date;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 在什么时间
 *
 * @author limaofeng
 */
@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class When {

  /** 类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "`WHEN`", length = 10)
  private WhenType type;
  /** 时间 */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "WHEN_ON_DATE")
  private Date onDate;
}
