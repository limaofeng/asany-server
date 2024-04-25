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
package cn.asany.cms.module;

import cn.asany.base.IModuleProperties;
import cn.asany.cms.module.dto.ArticleChannelImpObj;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 内容管理模块
 *
 * @author limaofeng
 */
@Data
@NoArgsConstructor
public class CmsModuleProperties implements IModuleProperties {
  @Setter private String type;
  @Setter private String rootCategory;
  private List<ArticleChannelImpObj> categories;

  public CmsModuleProperties(Map<String, String> properties) {
    this.rootCategory = properties.get("rootCategory");
  }

  @Override
  public String getType() {
    return "cms";
  }
}
