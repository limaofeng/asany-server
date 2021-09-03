package cn.asany.cms.article.bean.enums;

/**
 * 文章状态
 *
 * @author limaofeng
 */
public enum ArticleStatus {
  /** 草稿 */
  draft,
  /** 发布 */
  published,
  /** 待发布 */
  waitPublished,
  /** 待审核 */
  waitAudit,
  /** 审核不通过 */
  failureAudit
}
