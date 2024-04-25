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

import cn.asany.cms.article.domain.enums.CommentStatus;
import cn.asany.cms.article.domain.enums.CommentTargetType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * 评论表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-21 下午4:36:07
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "AUTH_COMMENT")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Comment extends BaseBusEntity {

  @Id
  @Column(name = "ID")
  @TableGenerator
  private Long id;

  /** 文章标题 */
  @Column(name = "TITLE", length = 200)
  private String title;

  /** 评论内容 */
  @Column(name = "CONTENT", nullable = false, columnDefinition = "TEXT")
  private String content;

  /** 评论状态 */
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", length = 20, nullable = false)
  private CommentStatus status;

  /** 评论完整路径 */
  @Column(name = "PATH", length = 1000)
  private String path;

  /** 引用的评论 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "FOR_COMMENT_ID", foreignKey = @ForeignKey(name = "FK_COMMENT_FOR_COMMENT"))
  private Comment forComment;

  /** 评论的下级评论 */
  @OneToMany(
      mappedBy = "forComment",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  @OrderBy("createdAt asc")
  private List<Comment> replyComments;

  /** 评论人ID */
  @Column(name = "UID", nullable = false)
  private String uid;

  /** 评论的类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TARGET_TYPE", length = 20)
  private CommentTargetType targetType;

  /** 评论的文章 */
  @Column(name = "TARGET_ID", length = 50)
  private String targetId;
}
