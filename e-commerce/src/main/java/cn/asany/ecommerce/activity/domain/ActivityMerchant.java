package cn.asany.ecommerce.activity.domain;

import cn.asany.ecommerce.activity.domain.enums.MerchantType;
import cn.asany.ecommerce.shop.domain.Merchant;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Table;
import javax.validation.constraints.Null;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import net.asany.jfantasy.framework.spring.validation.Operation;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EC_ACTIVITY_MERCHANT")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ActivityMerchant extends BaseBusEntity {

  @Id
  @Null(groups = Operation.Create.class)
  @Column(name = "ID", nullable = false)
  @TableGenerator
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20, nullable = false)
  private MerchantType type;

  /** 商户 */
  //  @Any(
  //      metaColumn = @Column(name = "TYPE", length = 20, insertable = false, updatable = false),
  //      fetch = FetchType.LAZY)
  //  @AnyMetaDef(
  //      idType = "long",
  //      metaType = "string",
  //      metaValues = {
  //        @MetaValue(targetEntity = Shop.class, value = Shop.TYPE_CODE),
  //        @MetaValue(targetEntity = Store.class, value = Store.TYPE_CODE)
  //      })
  //  @JoinColumn(name = "merchant", insertable = false, updatable = false)
  @Transient private Merchant merchant;

  /** 排序字段 */
  @Column(name = "_index")
  private Integer index;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ACTIVITY_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_EC_ACTIVITY_MERCHANT_ACTIVITY"))
  private Activity activity;
}
