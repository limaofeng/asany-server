package cn.asany.security.core.domain;

import cn.asany.security.core.domain.enums.GranteeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Grantee {
  /** 授权主体类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "GRANTEE_TYPE", length = 10)
  private GranteeType type;

  /** 授权主体值 */
  @Column(name = "GRANTEE_ID", length = 24)
  private String value;

  public static Grantee user(Long id) {
    return new Grantee(GranteeType.USER, String.valueOf(id));
  }

  public static Grantee valueOf(String authority) {
    int index = authority.indexOf("_");
    String type = authority.substring(0, index);
    String code = authority.substring(index + 1);
    return new Grantee(GranteeType.of(type.toLowerCase() + GranteeType.DELIMITER), code);
  }
}
