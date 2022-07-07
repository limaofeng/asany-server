package cn.asany.nuwa.app.graphql;

import cn.asany.nuwa.app.converter.ApplicationMenuConverter;
import cn.asany.nuwa.app.domain.ApplicationMenu;
import cn.asany.nuwa.app.graphql.input.ApplicationMenuCreateInput;
import cn.asany.nuwa.app.graphql.input.ApplicationMenuUpdateInput;
import cn.asany.nuwa.app.service.ApplicationMenuService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

/**
 * 应用菜单 维护接口
 *
 * @author limaofeng
 */
@Component
public class ApplicationMenuGraphQLMutationResolver implements GraphQLMutationResolver {

  private final ApplicationMenuService menuService;
  private final ApplicationMenuConverter menuConverter;

  public ApplicationMenuGraphQLMutationResolver(
      ApplicationMenuService menuService, ApplicationMenuConverter menuConverter) {
    this.menuService = menuService;
    this.menuConverter = menuConverter;
  }

  public ApplicationMenu createMenu(ApplicationMenuCreateInput input) {
    return this.menuService.create(menuConverter.toMenu(input));
  }

  public ApplicationMenu updateMenu(Long id, ApplicationMenuUpdateInput input, Boolean merge) {
    return this.menuService.update(id, menuConverter.toMenu(input), merge);
  }

  public Boolean deleteMenu(Long id) {
    this.menuService.delete(id);
    return Boolean.TRUE;
  }

  public ApplicationMenu moveMenu(Long id, Long parentMenu, int location) {
    return this.menuService.update(
        id,
        ApplicationMenu.builder()
            .parent(parentMenu == null ? null : ApplicationMenu.builder().id(parentMenu).build())
            .index(location)
            .build(),
        true);
  }
}
