package cn.asany.cardhop.contacts.bean;

import cn.asany.base.common.bean.Phone;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 手机
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-03-11 14:19
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CARDHOP_CONTACT_PHONE_NUMBER")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ContactPhoneNumber extends BaseBusEntity {

  @Id
  @Column(name = "ID", precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 主号码 */
  @Column(name = "IS_PRIMARY", nullable = false)
  private Boolean primary;
  /** 标签 */
  @Column(name = "LABEL", length = 30)
  private String label;
  /** 电话 */
  @Embedded private Phone phone;
  /** 员工 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "CONTACT_ID",
      foreignKey = @ForeignKey(name = "FK_CARDHOP_CONTACT_PHONE_CID"),
      nullable = false,
      updatable = false)
  private Contact contact;
}
