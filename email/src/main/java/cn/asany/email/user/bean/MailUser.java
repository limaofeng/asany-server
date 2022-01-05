package cn.asany.email.user.bean;

import cn.asany.email.domainlist.bean.JamesDomain;
import cn.asany.organization.core.bean.Organization;
import cn.asany.organization.employee.bean.Employee;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import javax.persistence.*;
import lombok.*;
import org.apache.james.user.api.model.User;
import org.hibernate.Hibernate;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * James User
 *
 * @author limaofeng
 */
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity(name = "JamesUser")
@Table(name = "JAMES_USER")
public class MailUser extends BaseBusEntity implements User {

  public static final String ALGORITHM_NONE = "NONE";
  public static final String ALGORITHM_MD5 = "MD5";
  public static final String ALGORITHM_SHA256 = "SHA-256";
  public static final String ALGORITHM_SHA512 = "SHA-512";

  @Id
  @Column(name = "USER_NAME", nullable = false, length = 100)
  private String name;

  @Basic
  @Column(name = "PASSWORD", nullable = false, length = 128)
  private String password;

  @Basic
  @Column(name = "PASSWORD_HASH_ALGORITHM", nullable = false, length = 100)
  private String alg;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "DOMAIN_ID", foreignKey = @ForeignKey(name = "FK_JAMES_USER_EMPLOYEE"))
  @ToString.Exclude
  private JamesDomain domain;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "USER_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_JAMES_USER_UID"))
  @ToString.Exclude
  private cn.asany.security.core.bean.User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "EMPLOYEE_ID", foreignKey = @ForeignKey(name = "FK_JAMES_USER_EMPLOYEE"))
  @ToString.Exclude
  private Employee employee;

  /** 所属组织 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ORGANIZATION_ID", foreignKey = @ForeignKey(name = "FK_JAMES_USER_ORG"))
  @ToString.Exclude
  private Organization organization;

  @Override
  public String toString() {
    return "[User " + name + "]";
  }

  @Override
  public String getUserName() {
    return name;
  }

  @Override
  public boolean verifyPassword(String pass) {
    final boolean result;
    if (pass == null) {
      result = password == null;
    } else {
      result = password != null && password.equals(hashPassword(pass, alg));
    }
    return result;
  }

  @Override
  public boolean setPassword(String newPass) {
    return false;
  }

  static String hashPassword(String password, String alg) {
    return chooseHashFunction(alg).apply(password);
  }

  interface PasswordHashFunction extends Function<String, String> {}

  private static PasswordHashFunction chooseHashFunction(String nullableAlgorithm) {
    String algorithm = Optional.ofNullable(nullableAlgorithm).orElse(ALGORITHM_MD5);
    if (ALGORITHM_NONE.equals(algorithm)) {
      return (password) -> password;
    }
    return (password) ->
        chooseHashing(algorithm).hashString(password, StandardCharsets.UTF_8).toString();
  }

  private static HashFunction chooseHashing(String algorithm) {
    switch (algorithm) {
      case ALGORITHM_MD5:
        return Hashing.md5();
      case ALGORITHM_SHA256:
        return Hashing.sha256();
      case ALGORITHM_SHA512:
        return Hashing.sha512();
      default:
        return Hashing.sha1();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    MailUser mailUser = (MailUser) o;
    return name != null && Objects.equals(name, mailUser.name);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
