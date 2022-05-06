package cn.asany.security.core.convert;

import cn.asany.security.core.bean.User;
import cn.asany.security.core.graphql.inputs.UserUpdateInput;
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
  @Mappings({})
  User toUser(UserUpdateInput input);
}
