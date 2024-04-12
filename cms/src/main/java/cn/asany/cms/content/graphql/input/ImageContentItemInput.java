package cn.asany.cms.content.graphql.input;

import cn.asany.cms.content.domain.ImageItem;
import cn.asany.storage.api.FileObject;
import lombok.Data;

@Data
public class ImageContentItemInput {
  private String url;
  private FileObject image;
  private String alt;
  private String title;
  private String description;
  private Integer index;

  public ImageItem toImageContentItem() {
    return ImageItem.builder()
        .image(this.image)
        .url(this.url)
        .alt(this.alt)
        .title(this.title)
        .description(this.description)
        .build();
  }
}
