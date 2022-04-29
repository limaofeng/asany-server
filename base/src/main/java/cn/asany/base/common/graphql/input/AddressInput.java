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
  /** 省 */
  private String province;
  /** 城市 */
  private String city;
  /** 区 */
  private String district;
  /** 街道 */
  private String street;
  /** 邮编 */
  private String postalCode;
  /** 详细地址 */
  private String detailedAddress;
}
