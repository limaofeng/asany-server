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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * 文章推荐位
 *
 * @author limaofeng
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CMS_ARTICLE_FEATURE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ArticleFeature extends BaseBusEntity {

  @Id
  @Column(name = "ID", length = 10)
  @TableGenerator
  private Long id;

  /** 编码 */
  @Column(name = "CODE")
  private String code;

  /** 名称 */
  @Column(name = "NAME", nullable = false, length = 150)
  private String name;

  /** 是否启用流程 true 启用，false 不启用 */
  @Column(name = "ENABLE_REVIEW")
  private Boolean needReview;

  /** 描述 */
  @Column(name = "DESCRIPTION")
  private String description;
}
