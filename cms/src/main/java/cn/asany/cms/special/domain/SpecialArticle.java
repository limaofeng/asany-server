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
package cn.asany.cms.special.domain;

import cn.asany.base.usertype.FileUserType;
import cn.asany.cms.article.domain.Article;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import javax.tools.FileObject;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "CMS_SPECIAL_ARTICLE")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "special", "article"})
public class SpecialArticle extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false)
  @TableGenerator
  private Long id;

  /** 期数 */
  @Column(name = "PERIODICAL", nullable = false)
  private String periodical;

  /** 标题 */
  @Column(name = "TITLE", nullable = false)
  private String title;

  /** 摘要 */
  @Column(name = "SUMMARY", length = 500, nullable = false)
  private String summary;

  /** 封面 */
  @Column(name = "COVER", length = 500)
  @Type(FileUserType.class)
  private FileObject cover;

  /** 发布日期 */
  @Column(name = "PUBLISH_DATE", length = 15, nullable = false)
  private String publishDate;

  /** 对应的文章 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "SPECIAL_ID",
      nullable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "FK_SPECIALARTICLE_SPECIAL"))
  private Special special;

  /** 对应的文章 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ARTICLE_ID",
      nullable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "FK_SPECIALARTICLE_ARTICLE"))
  private Article article;
}
