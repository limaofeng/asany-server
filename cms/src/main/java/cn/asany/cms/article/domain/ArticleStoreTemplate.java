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
package cn.asany.cms.article.domain;

import cn.asany.cms.content.domain.enums.ContentType;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;

/**
 * 文章存储模版
 *
 * @author limaofeng
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CMS_ARTICLE_STORE_TEMPLATE")
public class ArticleStoreTemplate extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, length = 20)
  private String id;

  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 内容类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "CONTENT_TYPE", length = 20)
  private ContentType contentType;

  /** 存储类 */
  @Column(name = "STORAGE_CLASS", length = 150)
  private String storageClass;

  /** 排序 */
  @Column(name = "SORT")
  private Integer index;

  /** 相关展示组件 */
  @Embedded private ArticleComponents components;
}
