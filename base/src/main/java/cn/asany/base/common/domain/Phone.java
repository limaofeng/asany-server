package cn.asany.base.common.domain;

import cn.asany.base.common.domain.enums.PhoneNumberStatus;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 电话
 *
 * @author limaofeng
 */
@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Phone {
  /** 状态 */
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", nullable = false, length = 20)
  private PhoneNumberStatus status;
  /** 电话 */
  @Column(name = "NUMBER", nullable = false, length = 25)
  private String number;
}
