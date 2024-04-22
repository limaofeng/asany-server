package cn.asany.security.auth.graphql.types;

import cn.asany.base.common.domain.Email;
import cn.asany.base.common.domain.Phone;
import cn.asany.security.core.domain.*;
import cn.asany.security.core.domain.enums.Sex;
import cn.asany.security.core.domain.enums.UserType;
import cn.asany.storage.api.FileObject;
import java.util.Date;
import java.util.List;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.jfantasy.framework.spring.validation.Operation;

/**
 * 当前用户
 *
 * @author limaofeng
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CurrentUser extends User {

  @Builder(builderMethodName = "CurrentUserBuilder")
  public CurrentUser(
      Long id,
      @NotEmpty(groups = {Operation.Create.class, Operation.Update.class})
          @Length(
              min = 6,
              max = 20,
              groups = {Operation.Create.class, Operation.Update.class})
          String username,
      String password,
      UserType userType,
      FileObject avatar,
      String nickname,
      String title,
      UserStatus status,
      Phone phone,
      Email email,
      Date birthday,
      Sex sex,
      String company,
      String location,
      String bio,
      Boolean enabled,
      Boolean accountNonExpired,
      Boolean accountNonLocked,
      Boolean credentialsNonExpired,
      Boolean forcePasswordReset,
      Date lockTime,
      Date lastLoginTime,
      String tenantId,
      List<Permission> permissions) {
    super(
        id,
        username,
        password,
        userType,
        avatar,
        nickname,
        title,
        status,
        phone,
        email,
        birthday,
        sex,
        company,
        location,
        bio,
        enabled,
        accountNonExpired,
        accountNonLocked,
        credentialsNonExpired,
        forcePasswordReset,
        lockTime,
        lastLoginTime,
        tenantId,
        permissions);
  }
}
