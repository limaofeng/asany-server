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
package cn.asany.cms.content.graphql.input;

import cn.asany.storage.api.FileObject;
import java.util.List;
import lombok.Data;

@Data
public class ArticleContentInput {
  private Long id;

  /** 标题 */
  private String title;

  /** 描述 */
  private String description;

  /** 引用地址 */
  private String url;

  /** * HTML 类型：HTML, MARKDOWN */
  private String type;

  /** 文本 */
  private String text;

  /** 文件大小 */
  private Long size;

  /** 图片集 */
  private List<ImageContentItemInput> images;

  /** 视频 */
  private FileObject video;

  /** 文档 */
  private FileObject document;
}
