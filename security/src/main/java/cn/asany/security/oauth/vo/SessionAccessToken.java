package cn.asany.security.oauth.vo;

import cn.asany.security.oauth.bean.ClientDevice;
import java.util.Date;
import lombok.Data;

/**
 * 会话
 *
 * @author limaofeng
 */
@Data
public class SessionAccessToken {
  /** ID */
  private Long id;
  /** 设备 */
  private ClientDevice device;
  /** IP 地址 */
  private String ip;
  /** 最后一次使用的 IP 地址 */
  private String lastIp;
  /** 登录时的位置 */
  private String location;
  /** 最后一次访问的位置 */
  private String lastLocation;
  /** 登录时间 */
  private Date loginTime;
  /** 最后一次访问时间 */
  private Date lastUsedTime;
  /** 令牌 */
  private String token;
  /** 过期时间 */
  private Date expiresAt;
  /** 客户端凭证 ID */
  private String client;
}
