package cn.asany.base.common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 地址
 *
 * @author limaofeng
 * @version V1.0
 */
@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address {

  /** 国家 */
  @Column(name = "COUNTRY", length = 30)
  private String country;
  /** 省 */
  @Column(name = "PROVINCE", length = 30)
  private String province;
  /** 城市 */
  @Column(name = "CITY", length = 30)
  private String city;
  /** 区 */
  @Column(name = "DISTRICT", length = 30)
  private String district;
  /** 街道 */
  @Column(name = "STREET", length = 30)
  private String street;
  /** 详细地址 */
  @Column(name = "DETAILED_ADDRESS", length = 100)
  private String detailedAddress;
  /** 邮编 */
  @Column(name = "POSTAL_CODE", length = 10)
  private String postalCode;
}
