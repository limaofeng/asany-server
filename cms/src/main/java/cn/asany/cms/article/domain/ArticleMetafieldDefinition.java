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

import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "CMS_ARTICLE_META_FIELD_DEFINITION",
    uniqueConstraints =
        @UniqueConstraint(
            name = "UK_ARTICLE_META_FIELD_DEFINITION",
            columnNames = {"ARTICLE_CATEGORY_ID", "NAMESPACE", "META_KEY"}))
public class ArticleMetafieldDefinition extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false)
  @TableGenerator
  private Long id;

  /** 字段 Key */
  @Column(name = "META_KEY", length = 50)
  private String key;

  /** 字段类型 */
  @Column(name = "TYPE", length = 50)
  private String type;

  /** 字段 namespace */
  @Column(name = "NAMESPACE", length = 50)
  private String namespace;

  /** 字段 描述 */
  @Column(name = "DESCRIPTION", length = 200)
  private String description;

  /** 关联文章 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ARTICLE_CATEGORY_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_CMS_ARTICLE_META_FIELD_DEFINITION_ARTICLE_CATEGORY_ID"))
  private ArticleCategory category;
}
