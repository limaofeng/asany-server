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

import cn.asany.cms.content.domain.ImageItem;
import cn.asany.storage.api.FileObject;
import lombok.Data;

@Data
public class ImageContentItemInput {
  private String url;
  private FileObject image;
  private String alt;
  private String title;
  private String description;
  private Integer index;

  public ImageItem toImageContentItem() {
    return ImageItem.builder()
        .image(this.image)
        .url(this.url)
        .alt(this.alt)
        .title(this.title)
        .description(this.description)
        .build();
  }
}
