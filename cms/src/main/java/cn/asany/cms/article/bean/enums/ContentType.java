package cn.asany.cms.article.bean.enums;

/** 内容存储类型 */
public enum ContentType {
  /** 对象 - 文件类型的文章时，应选择该类型 */
  json,
  /** 链接地址 - 链接文章类型时，应选择该类型 */
  link,
  /** HTML */
  html,
  /** Markdown 原始文件 */
  markdown,
  /** 附件地址 */
  file
}
