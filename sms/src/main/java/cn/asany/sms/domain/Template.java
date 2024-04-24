package cn.asany.sms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity(name = "SmsTemplate")
@Table(name = "SMS_TEMPLATE")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "id"})
public class Template extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, precision = 22)
  @TableGenerator
  private Long id;

  /** 模版名称 */
  @Column(name = "NAME", nullable = false, length = 50)
  private String name;

  /** 签名 */
  @Column(name = "SIGN", nullable = false, length = 20)
  private String sign;

  /** 模板号 */
  @Column(name = "CODE", length = 120)
  private String code;

  /** 模版内容 */
  @Column(name = "CONTENT", columnDefinition = "Text")
  private String content;
}
