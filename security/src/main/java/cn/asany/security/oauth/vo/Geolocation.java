package cn.asany.security.oauth.vo;

import lombok.Data;

@Data
public class Geolocation {
  /** ID */
  private String id;
  /** IP 地址 */
  private String internetProtocolAddress;
  /** 国家 */
  private String country;
  /** 省 / 州 */
  private String state;
  /** 城市 */
  private String city;
}
