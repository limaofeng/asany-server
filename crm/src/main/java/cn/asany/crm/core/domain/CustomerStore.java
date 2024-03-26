package cn.asany.crm.core.domain;

import cn.asany.base.common.domain.Address;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 客户门店
 *
 * @author limaofeng
 */
@Data
@Entity
@Table(name = "CRM_CUSTOMER_STORE")
@EqualsAndHashCode(callSuper = true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CustomerStore extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 门店编号 */
  @Column(name = "NO", length = 50)
  private String no;
  /** 门店名称 */
  @Column(name = "NAME", length = 50)
  private String name;
  /** 开业时间 */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "OPENING_DATE")
  private Date openingDate;
  /** 门店电话 */
  @Column(name = "PHONE", length = 20)
  private String phone;
  /** 门店地址 */
  @Embedded private Address address;
  /** 客户 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CUSTOMER_ID", foreignKey = @ForeignKey(name = "FK_CUSTOMER_STORE_CUSTOMER"))
  private Customer customer;
}