package cn.asany.cms.article.graphql.enums;

/**
 * 文章栏目收藏与点赞
 *
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
public enum ArticleChannelStarType {
  /** 关注 */
  follow("article_channel_follow");

  private final String value;

  ArticleChannelStarType(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
