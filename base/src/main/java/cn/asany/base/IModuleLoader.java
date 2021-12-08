package cn.asany.base;

/**
 * 应用模块加载器
 *
 * @author limaofeng
 */
public interface IModuleLoader {

  /**
   * 加载模块
   *
   * @param name 模块
   * @param module 模块配置
   */
  void load(String name, IModuleProperties module);
}
