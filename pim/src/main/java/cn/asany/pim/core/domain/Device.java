package cn.asany.pim.core.domain;

import cn.asany.base.common.domain.Address;
import cn.asany.base.common.domain.Owner;
import cn.asany.pim.core.domain.enums.DeviceOwnerType;
import cn.asany.pim.product.domain.Brand;
import cn.asany.pim.product.domain.Product;
import cn.asany.pim.product.domain.ProductVariant;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@DiscriminatorValue("DEVICE")
@EqualsAndHashCode(callSuper = true)
public class Device extends PhysicalAsset {

  /** 设备的序列号，通常由制造商提供，具有唯一性 */
  @Column(name = "SN", length = 50)
  private String sn;
  /** 品牌 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BRAND_ID", foreignKey = @ForeignKey(name = "FK_PIM_ASSET_BRAND"))
  private Brand brand;
  /** 型号 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "PRODUCT_ID",
      foreignKey = @ForeignKey(name = "FK_PIM_ASSET_DEVICE_PRODUCT_PID"))
  private Product product;
  /** 产品变种 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "PRODUCT_VARIANT_ID",
      foreignKey = @ForeignKey(name = "FK_PIM_ASSET_DEVICE_PRODUCT_VARIANT_ID"))
  private ProductVariant productVariant;
  /** 购买日期 */
  @Column(name = "PURCHASE_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date purchaseDate;
  /** 保修截止日期 */
  @Column(name = "EXPIRES_AT")
  @Temporal(TemporalType.TIMESTAMP)
  private Date expiresAt;
  /** 位置 */
  @Embedded private Address location;
  /** 所有者 */
  @Embedded private Owner<DeviceOwnerType> owner;
}
