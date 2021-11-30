package cn.asany.nuwa.app.converter;

import cn.asany.nuwa.app.bean.Application;
import cn.asany.nuwa.app.bean.ApplicationMenu;
import cn.asany.nuwa.app.bean.ApplicationRoute;
import cn.asany.nuwa.app.bean.Routespace;
import cn.asany.nuwa.app.graphql.input.ApplicationCreateInput;
import cn.asany.nuwa.app.service.dto.NativeApplication;
import cn.asany.nuwa.app.service.dto.NuwaMenu;
import cn.asany.nuwa.app.service.dto.NuwaRoute;
import cn.asany.nuwa.app.service.dto.OAuthApplication;
import cn.asany.nuwa.template.bean.ApplicationTemplateMenu;
import cn.asany.nuwa.template.bean.ApplicationTemplateRoute;
import cn.asany.ui.resources.bean.toy.ComponentData;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;
import java.util.List;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.StringUtil;
import org.mapstruct.*;

/**
 * 应用 转换器
 *
 * @author limaofeng
 */
@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ApplicationConverter {

  /**
   * 路由转换
   *
   * @param routes List<ApplicationTemplateRoute>
   * @param routespace Routespace
   * @return ApplicationRoute
   */
  @IterableMapping(elementTargetType = ApplicationRoute.class)
  List<ApplicationRoute> toRoutes(
      List<ApplicationTemplateRoute> routes, @Context Routespace routespace);

  /**
   * 将 ApplicationTemplateRoute 转换为 ApplicationRoute
   *
   * @param route 路由模版
   * @param routespace 空间
   * @return ApplicationRoute
   */
  @Mappings({
    @Mapping(target = "id", ignore = true),
    @Mapping(target = "parent", ignore = true),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdAt", ignore = true),
    @Mapping(target = "updatedBy", ignore = true),
    @Mapping(target = "updatedAt", ignore = true),
    @Mapping(target = "application", ignore = true),
    @Mapping(target = "space", source = "id", qualifiedByName = "routespace"),
  })
  ApplicationRoute toRoute(ApplicationTemplateRoute route, @Context Routespace routespace);

  /**
   * 将 NuwaRoute 转为 ApplicationRoute 对象
   *
   * @param route NuwaRoute 路由
   * @return ApplicationRoute
   */
  @Mappings({
    @Mapping(target = "layout", source = "settings"),
    @Mapping(
        target = "component.blocks",
        source = "component.blocks",
        qualifiedByName = "componentDataBlocks")
  })
  ApplicationRoute toRouteFromNuwa(NuwaRoute route);

  @Named("componentDataBlocks")
  default List<ComponentData> componentDataBlocks(String blocks) {
    if (StringUtil.isBlank(blocks)) {
      return new ArrayList<>();
    }
    return JSON.deserialize(blocks, new TypeReference<List<ComponentData>>() {});
  }

  /**
   * 路由
   *
   * @param route 路由
   * @param routespace 路由空间
   * @return Routespace
   */
  @Named("routespace")
  default Routespace routespace(Object route, @Context Routespace routespace) {
    return routespace;
  }

  /**
   * 将 ApplicationCreate 转换为 NativeApplication 对象
   *
   * @param input ApplicationCreateInput
   * @return NativeApplication 对象
   */
  @Mappings({})
  NativeApplication toNativeApplication(ApplicationCreateInput input);

  @Mappings({
    @Mapping(target = "id", ignore = true),
    @Mapping(target = "parent", ignore = true),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdAt", ignore = true),
    @Mapping(target = "updatedBy", ignore = true),
    @Mapping(target = "updatedAt", ignore = true),
    @Mapping(target = "application", ignore = true),
  })
  ApplicationMenu toMenuFromTemplate(ApplicationTemplateMenu menu);

  /**
   * YML 导入时的转换对象
   *
   * @param nuwaMenu 导入对象
   * @return Set<ApplicationMenu>
   */
  @Mappings({
    @Mapping(target = "children", ignore = true),
    @Mapping(
        target = "component.blocks",
        source = "component.blocks",
        qualifiedByName = "componentDataBlocks")
  })
  ApplicationMenu toMenuFromNuwa(NuwaMenu nuwaMenu);

  /**
   * 从 模版 导入
   *
   * @param templateMenus 模版对象
   * @return List<ApplicationMenu>
   */
  List<ApplicationMenu> toMenusFromTemplate(List<ApplicationTemplateMenu> templateMenus);

  @Mappings({})
  Application oauthAppToApp(OAuthApplication app);
}
