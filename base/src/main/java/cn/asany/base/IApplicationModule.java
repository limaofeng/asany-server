package cn.asany.base;

import java.util.Map;

/**
 * 应用模块
 *
 * @author limaofeng
 */
public interface IApplicationModule<T extends IModuleProperties> {

  /**
   * 模块名称 (唯一)
   *
   * @return String
   */
  String name();

  /**
   * 加载模块
   *
   * @param config 模块配置
   */
  Map<String, String> configuration(ModuleConfig<T> config);
}
