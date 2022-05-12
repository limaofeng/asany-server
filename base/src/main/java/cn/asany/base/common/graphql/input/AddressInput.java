package cn.asany.base.common.graphql.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressInput {
  /** 国家 */
  private String country;
  /** 国家名称 */
  private String countryName;
  /** 省 */
  private String province;
  /** 省名称 */
  private String provinceName;
  /** 城市 */
  private String city;
  /** 城市名称 */
  private String cityName;
  /** 区 */
  private String district;
  /** 区名称 */
  private String districtName;
  /** 街道 */
  private String street;
  /** 街道名称 */
  private String streetName;
  /** 邮编 */
  private String postalCode;
  /** 详细地址 */
  private String detailedAddress;
}
