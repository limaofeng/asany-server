package cn.asany.security.oauth.bean;

import eu.bitwalker.useragentutils.DeviceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDevice {
  private String userAgent;
  private String browser;
  private DeviceType type;
  private String operatingSystem;
}
