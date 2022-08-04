package cn.asany.cms.article.graphql.enums;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
public enum CommentStarType {
  /** 点赞 */
  likes("comment_likes");

  private final String value;

  CommentStarType(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
