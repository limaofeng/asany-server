package cn.asany.storage.data.bean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * 图片
 *
 * @author limaofeng
 */
public class Image extends FileDetail implements Comparable<Image> {

  /** 排序 */
  private Integer sort;

  public Image() {}

  public Image(FileDetail fileDetail) {
    this.setPath(fileDetail.getPath());
    this.setMimeType(fileDetail.getMimeType());
    this.setDescription(fileDetail.getDescription());
    this.setName(fileDetail.getName());
    this.setStorageConfig(fileDetail.getStorageConfig());
    this.setMd5(fileDetail.getMd5());
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().appendSuper(super.hashCode()).append(this.getPath()).toHashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Image) {
      Image image = (Image) o;
      return new EqualsBuilder()
          .appendSuper(super.equals(o))
          .append(this.getPath(), image.getPath())
          .isEquals();
    }
    return false;
  }

  @Override
  public int compareTo(@NotNull Image image) {
    if (image.getSort() == null || this.getSort() == null) {
      return -1;
    }
    return this.getSort().compareTo(image.getSort());
  }

  public Integer getSort() {
    return sort;
  }

  public void setSort(Integer sort) {
    this.sort = sort;
  }
}
