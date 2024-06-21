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
package cn.asany.cms.article.graphql.input;

import cn.asany.cms.article.domain.enums.PromptType;
import cn.asany.storage.api.FileObject;
import java.util.List;
import lombok.Data;

/**
 * 文章栏目新增对象
 *
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
@Data
public class ArticleCategoryInput {
  private String name;
  private String description;
  private String slug;
  private Integer index;
  private List<PermissionInput> permissions;
  private Long parent;
  private FileObject image;
  private PromptType promptType;
  private Boolean isCommentApprove;
  private String approveId;
  private Long storeTemplate;
  private List<ArticleMetafieldInput> metafields;
  private ArticleMetadataInput metadata;
  private PageComponentInput page;
}
