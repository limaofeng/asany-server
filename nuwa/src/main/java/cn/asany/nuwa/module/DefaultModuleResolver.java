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
package cn.asany.nuwa.module;

import cn.asany.base.IApplicationModule;
import cn.asany.base.IModuleProperties;
import cn.asany.base.ModuleResolver;
import cn.asany.nuwa.YamlUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.asany.jfantasy.framework.util.common.ClassUtil;

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
