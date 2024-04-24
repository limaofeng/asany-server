package cn.asany.crm.core.domain;

import cn.asany.base.common.Ownership;
import cn.asany.base.common.domain.Address;
import cn.asany.base.common.domain.ContactInformation;
import cn.asany.base.common.domain.Geolocation;
import jakarta.persistence.*;
import java.util.Date;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 客户门店
 *
 * @author limaofeng
 */
@Data
@Entity
@Builder
@Table(name = "CRM_CUSTOMER_STORE")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CustomerStore extends BaseBusEntity implements Ownership {

  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @TableGenerator
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

  /** 门店位置 (经纬坐标) */
  @Embedded private Geolocation location;

  /** 门店联系方式 */
  @Embedded private ContactInformation contactInfo;

  /** 客户 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CUSTOMER_ID", foreignKey = @ForeignKey(name = "FK_CUSTOMER_STORE_CUSTOMER"))
  private Customer customer;
}
