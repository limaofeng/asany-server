package cn.asany.security.oauth.vo;

import java.util.Date;
import java.util.Set;
import lombok.Data;

/**
 * 个人 Token
 *
 * @author limaofeng
 */
@Data
public class PersonalAccessToken {
  /** ID */
  private String id;
  /** 名称 */
  private String name;
  /** 生成时间 */
  private Date issuedAt;
  /** 过期时间 */
  private Date expiresAt;
  /** 范围 */
  private Set<String> scopes;
}
