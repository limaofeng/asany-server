package cn.asany.cms.article.domain;

import cn.asany.cms.article.domain.enums.CommentStatus;
import cn.asany.cms.article.domain.enums.CommentTargetType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

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
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "comment_gen")
  @TableGenerator(
      name = "comment_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "auth_comment:id",
      valueColumnName = "gen_value")
  @Column(name = "ID")
  private Long id;
  /** 文章标题 */
  @Column(name = "TITLE")
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
