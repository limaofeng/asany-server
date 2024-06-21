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
package cn.asany.nuwa.app.graphql.input;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建应用的表单
 *
 * @author limaofeng
 */
@Data
@NoArgsConstructor
public class ApplicationCreateInput {
  /** 名称 */
  private String name;

  /** 简介 */
  private String description;
}
