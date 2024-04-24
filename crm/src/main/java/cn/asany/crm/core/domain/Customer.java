package cn.asany.crm.core.domain;

import cn.asany.base.common.Ownership;
import cn.asany.base.common.domain.ContactInformation;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
  @TableGenerator
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
