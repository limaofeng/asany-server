package cn.asany.crm.core.domain;

import cn.asany.base.common.Ownership;
import cn.asany.base.common.domain.ContactInformation;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 客户
 *
 * @author limaofeng
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CRM_CUSTOMER")
@EqualsAndHashCode(callSuper = true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Customer extends BaseBusEntity implements Ownership {

  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 客户名称 */
  @Column(name = "NAME", length = 50)
  private String name;
  /** 联系方式 */
  @Embedded private ContactInformation contactInfo;
  /** 客户门店 */
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.REMOVE)
  private List<CustomerStore> stores;
}
