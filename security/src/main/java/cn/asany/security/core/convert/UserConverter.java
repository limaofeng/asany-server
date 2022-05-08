package cn.asany.security.core.convert;

import cn.asany.base.common.bean.Email;
import cn.asany.base.common.bean.Phone;
import cn.asany.security.auth.graphql.types.CurrentUser;
import cn.asany.security.core.bean.User;
import cn.asany.security.core.graphql.input.UserUpdateInput;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserConverter {

  /**
   * 更新用户
   *
   * @param input 输入
   * @return 菜单
   */
  @Mappings({
    @Mapping(source = "name", target = "nickName"),
    @Mapping(target = "phone", qualifiedByName = "toPhone"),
    @Mapping(target = "email", qualifiedByName = "toEmail")
  })
  User toUser(UserUpdateInput input);

  @Mappings({})
  CurrentUser toCurrentUser(User user);

  @Named("toPhone")
  default Phone toPhone(String source) {
    if (source == null) {
      return null;
    }
    return Phone.builder().number(source).build();
  }

  @Named("toEmail")
  default Email toEmail(String source) {
    if (source == null) {
      return null;
    }
    return Email.builder().address(source).build();
  }
}
