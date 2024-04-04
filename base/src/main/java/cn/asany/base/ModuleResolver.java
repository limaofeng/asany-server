package cn.asany.base;

/**
 * 应用模块加载器
 *
 * @author limaofeng
 */
public interface ModuleResolver {

  /**
   * 加载模块
   *
   * @param name 模块
   */
  IApplicationModule<IModuleProperties> resolve(String name);
}
