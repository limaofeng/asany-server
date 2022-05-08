package cn.asany.security.auth.graphql.types;

import cn.asany.base.common.bean.Email;
import cn.asany.base.common.bean.Phone;
import cn.asany.security.core.bean.GrantPermission;
import cn.asany.security.core.bean.Role;
import cn.asany.security.core.bean.User;
import cn.asany.security.core.bean.UserStatus;
import cn.asany.security.core.bean.enums.Sex;
import cn.asany.security.core.bean.enums.UserType;
import cn.asany.storage.api.FileObject;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.jfantasy.framework.spring.validation.Operation;

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
      String nickName,
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
      Date lockTime,
      Date lastLoginTime,
      List<Role> roles,
      List<GrantPermission> grants) {
    super(
        id,
        username,
        password,
        userType,
        avatar,
        nickName,
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
        lockTime,
        lastLoginTime,
        roles,
        grants);
  }
}
