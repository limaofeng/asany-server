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
package cn.asany.cms.module.dto;

import java.util.List;
import lombok.Data;

/**
 * ArticleChannel DTO
 *
 * @author limaofeng
 */
@Data
public class ArticleChannelImpObj {
  private String icon;
  private String name;
  private String description;
  private String slug;
  private List<ArticleChannelImpObj> children;
  private String storeTemplate;
  private List<ArticleImpObj> articles;
}
