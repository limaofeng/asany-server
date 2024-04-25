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

import cn.asany.cms.article.domain.enums.InteractionType;
import jakarta.persistence.*;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ARTICLE_INTERACTIONS")
public class ArticleInteraction {
  @Id
  @Column(name = "ID", nullable = false)
  @TableGenerator
  private Long id;

  /** 交互类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", nullable = false)
  private InteractionType type;

  /** 对于注册用户 */
  @Column(name = "USER_ID")
  private Long userId;

  /** 对于匿名用户 */
  @Column(name = "SESSION_ID", length = 120)
  private String sessionId;

  /** 时间戳 */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "TIMESTAMP", nullable = false)
  private Date timestamp;

  /** 阅读时长, 只在 READS 时有效 */
  @Column(name = "READING_TIME")
  private Long readingTime;

  /** 文章 */
  @ManyToOne
  @JoinColumn(
      name = "ARTICLE_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_ARTICLE_INTERACTIONS_ARTICLE"))
  private Article article;
}
