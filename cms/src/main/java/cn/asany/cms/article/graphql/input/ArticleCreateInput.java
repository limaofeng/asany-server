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

import cn.asany.cms.article.domain.enums.ArticleStatus;
import cn.asany.cms.article.domain.enums.ArticleType;
import cn.asany.cms.content.graphql.input.ArticleContentInput;
import cn.asany.storage.api.FileObject;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * 文章新增对象
 *
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
@Data
public class ArticleCreateInput {
  private String title;
  private ArticleType type;
  private String url;
  private ArticleStatus status;
  private ArticleContentInput content;
  private String summary;
  private FileObject image;
  private List<Long> tags;
  private List<String> features;
  private String author;
  private String organization;
  private Date publishedAt;
  private List<FileObject> attachments;
  private List<PermissionInput> permissions;
  private List<String> access;
  private Long category;
  private Boolean validity;
  private Date validityStartDate;
  private Date validityEndDate;
  private List<ArticleMetafieldInput> metafields;
}
