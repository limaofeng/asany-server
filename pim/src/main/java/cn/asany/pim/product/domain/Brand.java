package cn.asany.pim.product.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
