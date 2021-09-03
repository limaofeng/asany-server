package cn.asany.cms.article.graphql.enums;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-08-19 13:50
 */
public enum ArticlePermissionKey {
  /** 点击数 */
  viewer("ARTIVLE_VIEWER"),
  /** 阅读数 */
  downloader("ARTIVLE_DOWNLOADER"),
  /** 点赞数 */
  printer("ARTICLE_PRINTER");

  private final String value;

  ArticlePermissionKey(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
