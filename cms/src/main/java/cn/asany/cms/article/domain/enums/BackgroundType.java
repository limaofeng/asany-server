package cn.asany.cms.article.domain.enums;

/**
 * 媒体类型
 *
 * @author limaofeng
 */
public enum BackgroundType {
  /** 图片 */
  IMAGE,
  /** 视频 */
  VIDEO;

  public static BackgroundType resolve(String contentType) {
    if (contentType.startsWith(IMAGE.name().toLowerCase())) {
      return BackgroundType.IMAGE;
    }
    if (contentType.startsWith(VIDEO.name().toLowerCase())) {
      return BackgroundType.VIDEO;
    }
    return null;
  }
}
