package cn.asany.cms.article.bean.enums;

/**
 * 文章状态
 *
 * @author limaofeng
 */
public enum ArticleStatus {
  /** 草稿 */
  DRAFT,
  /** 发布 */
  PUBLISHED,
  /** 待发布 */
  SCHEDULED,
  /** 待审核 */
  waitAudit,
  /** 审核不通过 */
  failureAudit
}
