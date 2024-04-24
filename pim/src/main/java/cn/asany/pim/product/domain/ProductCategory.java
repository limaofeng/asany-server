package cn.asany.pim.product.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "PIM_PRODUCT_CATEGORY")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductCategory extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @TableGenerator
  private Long id;
}
