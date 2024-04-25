/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
   * 安装模块
   *
   * @param config 模块配置
   * @return Map
   */
  Map<String, String> install(ModuleConfig<T> config);

  /**
   * 卸载模块
   *
   * @param config 模块配置
   */
  void uninstall(ModuleConfig<T> config);
}
