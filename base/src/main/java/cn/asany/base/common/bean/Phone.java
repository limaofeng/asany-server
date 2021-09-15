package cn.asany.base.common.bean;

import cn.asany.base.common.bean.enums.PhoneNumberStatus;
import javax.persistence.*;

/**
 * 电话
 *
 * @author limaofeng
 */
@Embeddable
public class Phone {
  /** 状态 */
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", nullable = false, length = 20)
  private PhoneNumberStatus status;
  /** 电话 */
  @Column(name = "NUMBER", nullable = false, length = 25)
  private String number;

  public PhoneNumberStatus getStatus() {
    return status;
  }

  public void setStatus(PhoneNumberStatus status) {
    this.status = status;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }
}
