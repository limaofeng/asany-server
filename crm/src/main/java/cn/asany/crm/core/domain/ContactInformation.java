package cn.asany.crm.core.domain;

import cn.asany.base.common.domain.Address;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.jfantasy.framework.dao.BaseBusEntity;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "CRM_CUSTOMER_CONTACT")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ContactInformation extends BaseBusEntity {

  @Id
  @Column(name = "CUSTOMER_ID", nullable = false, updatable = false)
  @GenericGenerator(
      name = "customerContactPkGenerator",
      strategy = "foreign",
      parameters = {@Parameter(name = "property", value = "customer")})
  @GeneratedValue(generator = "customerContactPkGenerator")
  private Long id;

  @Column(name = "NAME", length = 50)
  private String name;
  /** 邮箱 */
  @Column(name = "EMAIL", length = 50)
  private String email;
  /** 电话 */
  @Column(name = "PHONE", length = 20)
  private String phone;
  /** 地址 */
  @Embedded Address address;

  @OneToOne(fetch = FetchType.LAZY, targetEntity = Customer.class, mappedBy = "contact")
  @ToString.Exclude
  private Customer customer;
}
