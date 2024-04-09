package cn.asany.cms.content.graphql.input;

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
}
