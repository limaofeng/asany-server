package cn.asany.cms.article.graphql.enums;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019/11/13 11:42 上午
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
