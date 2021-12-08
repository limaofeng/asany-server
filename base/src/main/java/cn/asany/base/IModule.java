package cn.asany.base;

/**
 * 应用模块
 *
 * @author limaofeng
 */
public interface IModule<T extends IModuleProperties> {

  /**
   * 模块名称 (唯一)
   *
   * @return String
   */
  String name();

  /**
   * 加载模块
   *
   * @param properties
   */
  void load(T properties);
}
