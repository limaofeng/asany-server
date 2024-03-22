package cn.asany.pim.product.domain;

import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jfantasy.framework.dao.BaseBusEntity;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "PIM_BRAND")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Brand extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  private String id;

  @Column(name = "NAME", length = 20, nullable = false)
  private String name;
}
