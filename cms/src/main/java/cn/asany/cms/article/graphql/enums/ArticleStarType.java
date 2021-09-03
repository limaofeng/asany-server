package cn.asany.cms.article.graphql.enums;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-08-14 11:44
 */
public enum ArticleStarType {
  /** 点击数 */
  clicks("article_clicks", null),
  /** 阅读数 */
  reads("article_reads", "ARTICLE_VIEWER"),
  /** 点赞数 */
  likes("article_likes", null),
  /** 收藏 */
  favorites("article_favorites", null);

  private final String value;
  private final String permissionKey;

  ArticleStarType(String value, String permissionKey) {
    this.value = value;
    this.permissionKey = permissionKey;
  }

  public String getValue() {
    return this.value;
  }

  public String getPermissionKey() {
    return this.permissionKey;
  }
}
