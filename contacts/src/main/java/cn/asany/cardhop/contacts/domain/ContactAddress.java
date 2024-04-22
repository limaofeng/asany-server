package cn.asany.cardhop.contacts.domain;

import cn.asany.base.common.domain.Address;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 地址表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2022-3-15 上午11:11:55
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "CARDHOP_CONTACT_ADDRESS")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class ContactAddress extends BaseBusEntity {
  @Id
  @Column(name = "ID", precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  @Column(name = "IS_PRIMARY", nullable = false)
  private Boolean primary;

  @Column(name = "LABEL", length = 30)
  private String label;

  @Embedded private Address address;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "CONTACT_ID",
      foreignKey = @ForeignKey(name = "FK_CARDHOP_CONTACT_ADDRESS_CID"),
      nullable = false,
      updatable = false)
  private Contact contact;
}
