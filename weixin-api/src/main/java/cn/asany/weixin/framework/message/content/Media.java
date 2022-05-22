package cn.asany.weixin.framework.message.content;

import cn.asany.storage.api.FileObject;

/** 微信媒体消息 */
public class Media {

  /** 媒体文件类型 */
  public enum Type {
    /** 图片 */
    image,
    /** 语音 */
    voice,
    /** 视频 */
    video,
    /** 缩略图 */
    thumb
  }

  private String id;

  private Media.Type type;

  private String format;

  private FileObject fileItem;

  public Media() {}

  public Media(String id) {
    this.id = id;
  }

  public Media(String id, String format) {
    this.id = id;
    this.format = format;
  }

  public Media(FileObject fileItem, Type type) {
    this.fileItem = fileItem;
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public String getFormat() {
    return format;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public Object getFileItem() {
    return fileItem;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public void setFileItem(FileObject fileItem) {
    this.fileItem = fileItem;
  }

  @Override
  public String toString() {
    return "Media{"
        + "id='"
        + id
        + '\''
        + ", type="
        + type
        + ", format='"
        + format
        + '\''
        + ", fileItem="
        + fileItem
        + '}';
  }
}
