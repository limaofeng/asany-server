package cn.asany.nuwa.app.converter;

import cn.asany.nuwa.app.domain.ApplicationMenu;
import cn.asany.nuwa.app.graphql.input.ApplicationMenuCreateInput;
import cn.asany.nuwa.app.graphql.input.ApplicationMenuUpdateInput;
import org.mapstruct.*;

/**
 * 应用菜单转换器 <br>
 *
 * @author limaofeng
 */
@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ApplicationMenuConverter {

  /**
   * 新增表单转业务对象
   *
   * @param input 输入
   * @return 菜单
   */
  @Mappings({
    @Mapping(target = "application.id", source = "application"),
    @Mapping(target = "parent.id", source = "parentMenu")
  })
  ApplicationMenu toMenu(ApplicationMenuCreateInput input);

  /**
   * 更新表单转业务对象
   *
   * @param input 输入
   * @return 菜单
   */
  @Mappings({
    @Mapping(target = "application.id", source = "application"),
    @Mapping(target = "parent.id", source = "parentMenu")
  })
  ApplicationMenu toMenu(ApplicationMenuUpdateInput input);
}
