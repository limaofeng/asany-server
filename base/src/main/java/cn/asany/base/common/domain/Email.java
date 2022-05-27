package cn.asany.base.common.domain;

import cn.asany.base.common.domain.enums.EmailStatus;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邮箱
 *
 * @author limaofeng
 */
@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Email {
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", nullable = false, length = 20)
  private EmailStatus status;
  /** 邮箱 */
  @Column(name = "ADDRESS", nullable = false, length = 25)
  private String address;
}
