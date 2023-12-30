package cn.asany.security.core.convert;

import cn.asany.security.core.domain.Group;
import cn.asany.security.core.graphql.input.GroupCreateInput;
import cn.asany.security.core.graphql.input.GroupUpdateInput;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * 用户组转换器
 *
 * @author limaofeng
 */
@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface GroupConverter {

  /**
   * 用户组
   *
   * @param input 输入对象
   * @return UserGroup
   */
  Group toGroup(GroupUpdateInput input);

  /**
   * 将 GroupCreateInput 转换为 用户组
   *
   * @param input GroupCreateInput
   * @return Group
   */
  Group toGroup(GroupCreateInput input);
}