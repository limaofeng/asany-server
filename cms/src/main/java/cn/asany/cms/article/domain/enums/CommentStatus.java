package cn.asany.cms.article.domain.enums;

/**
 * 评论状态
 *
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
public enum CommentStatus {
  /** 等待检测或审核 */
  pending,
  /** 审核通过 */
  published,
  /** 评论被删除 */
  removed,
  /** 审批不通过 */
  spam,
  /** 等待审批 */
  unapproved
}
