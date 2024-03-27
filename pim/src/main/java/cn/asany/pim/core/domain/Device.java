package cn.asany.pim.core.domain;

import cn.asany.base.common.domain.Address;
import cn.asany.base.common.domain.ContactInformation;
import cn.asany.base.common.domain.Geolocation;
import cn.asany.base.common.domain.Owner;
import cn.asany.pim.core.domain.enums.DeviceOwnerType;
import cn.asany.pim.product.domain.Brand;
import cn.asany.pim.product.domain.Product;
import cn.asany.pim.product.domain.ProductVariant;
import java.util.Date;
import java.util.List;
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
  /** 保修卡 */
  @OneToMany(mappedBy = "device", fetch = FetchType.LAZY)
  private List<WarrantyCard> warrantyCards;
  /** 位置 */
  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "country", column = @Column(name = "LOCATION_COUNTRY", length = 30)),
    @AttributeOverride(
        name = "province",
        column = @Column(name = "LOCATION_PROVINCE", length = 30)),
    @AttributeOverride(name = "city", column = @Column(name = "LOCATION_CITY", length = 30)),
    @AttributeOverride(
        name = "district",
        column = @Column(name = "LOCATION_DISTRICT", length = 30)),
    @AttributeOverride(name = "street", column = @Column(name = "LOCATION_STREET", length = 30)),
    @AttributeOverride(
        name = "detailedAddress",
        column = @Column(name = "LOCATION_DETAILED_ADDRESS", length = 100)),
    @AttributeOverride(
        name = "postalCode",
        column = @Column(name = "LOCATION_POSTAL_CODE", length = 10))
  })
  private Address address;
  /** 门店位置 (经纬坐标) */
  @Embedded private Geolocation location;
  /** 设备负责人联系方式 */
  @Embedded private ContactInformation contactInfo;

  /** 所有者 */
  @Embedded private Owner<DeviceOwnerType> owner;
}
