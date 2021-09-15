package cn.asany.base.common.bean;

import cn.asany.base.common.bean.enums.EmailStatus;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * 邮箱
 *
 * @author limaofeng
 */
@Embeddable
public class Email {
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", nullable = false, length = 20)
  private EmailStatus status;
  /** 邮箱 */
  @Column(name = "ADDRESS", nullable = false, length = 25)
  private String address;

  public EmailStatus getStatus() {
    return status;
  }

  public void setStatus(EmailStatus status) {
    this.status = status;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}
