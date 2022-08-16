package cn.asany.sms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "SMS_TEMPLATE")
@GenericGenerator(name = "template_gen", strategy = "fantasy-sequence")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "id"})
public class Template extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, precision = 22)
  @GeneratedValue(generator = "template_gen")
  private Long id;
  /** 模版名称 */
  @Column(name = "NAME", nullable = false, length = 50)
  private String name;
  /** 签名 */
  @Column(name = "SIGN", nullable = false, length = 20)
  private String sign;
  /** 模板号 */
  @Column(name = "CODE")
  private String code;
  /** 模版内容 */
  @Column(name = "CONTENT", columnDefinition = "Text")
  private String content;
}
