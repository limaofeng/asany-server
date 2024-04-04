package cn.asany.nuwa.module;

import cn.asany.base.IApplicationModule;
import cn.asany.base.IModuleProperties;
import cn.asany.base.ModuleResolver;
import cn.asany.nuwa.YamlUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfantasy.framework.util.common.ClassUtil;

/**
 * 默认模块加载器
 *
 * @author limaofeng
 */
public class DefaultModuleResolver implements ModuleResolver {

  private final Map<String, IApplicationModule<? extends IModuleProperties>> modules =
      new HashMap<>();

  public DefaultModuleResolver(List<IApplicationModule<? extends IModuleProperties>> modules) {
    for (IApplicationModule<? extends IModuleProperties> module : modules) {
      this.modules.put(module.name(), module);
      Class<IModuleProperties> clazz =
          ClassUtil.getInterfaceGenricType(module.getClass(), IApplicationModule.class, 0);
      YamlUtils.addModuleClass(module.name(), clazz);
    }
  }

  @Override
  public IApplicationModule<IModuleProperties> resolve(String name) {
    return (IApplicationModule<IModuleProperties>) this.modules.get(name);
  }
}
