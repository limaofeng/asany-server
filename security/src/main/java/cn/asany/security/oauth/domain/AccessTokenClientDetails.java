package cn.asany.security.oauth.domain;

import cn.asany.security.oauth.domain.converter.DeviceConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenClientDetails {
  /** 访问IP */
  @Column(name = "CLIENT_IP", length = 50)
  private String ip;
  /** 访问位置 */
  @Column(name = "CLIENT_LOCATION", length = 300)
  private String location;
  /** 操作系统和浏览器版本 */
  @Column(name = "CLIENT_DEVICE", length = 500, columnDefinition = "JSON")
  @Convert(converter = DeviceConverter.class)
  private ClientDevice device;
  /** 最后访问IP */
  @Column(name = "CLIENT_LAST_IP", length = 50)
  private String lastIp;
  /** 最后访问位置 */
  @Column(name = "CLIENT_LAST_LOCATION", length = 300)
  private String lastLocation;
}
