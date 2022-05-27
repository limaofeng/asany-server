package cn.asany.security.core.domain;

import javax.persistence.Column;
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
public class GrantPermissionValue {
  /** 被授权者的类型 */
  @Column(name = "GRANTEE_TYPE", nullable = false, length = 50)
  private String GranteeType;
  /** 被授权者的值 */
  @Column(name = "VALUE", nullable = false, length = 50)
  private String value;
}
