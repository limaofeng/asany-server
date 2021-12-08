package cn.asany.nuwa.module;

import cn.asany.base.IModule;
import cn.asany.base.IModuleLoader;
import cn.asany.base.IModuleProperties;
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
public class DefaultModuleLoader implements IModuleLoader {

  private Map<String, IModule> modules = new HashMap<>();

  public DefaultModuleLoader(List<IModule> modules) {
    for (IModule module : modules) {
      this.modules.put(module.name(), module);
      Class<IModuleProperties> clazz =
          ClassUtil.getInterfaceGenricType(module.getClass(), IModule.class, 0);
      YamlUtils.addModuleClass(module.name(), clazz);
    }
  }

  @Override
  public void load(String name, IModuleProperties properties) {
    this.modules.get(name).load(properties);
  }
}
