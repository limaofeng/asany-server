package cn.asany.cardhop.contacts.domain;

import cn.asany.base.common.domain.Phone;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * 手机
 *
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
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
  @TableGenerator
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
