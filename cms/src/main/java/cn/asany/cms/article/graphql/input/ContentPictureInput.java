package cn.asany.cms.article.graphql.input;

import cn.asany.storage.api.FileObject;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContentPictureInput {
  private FileObject picture;
  private String digest;
}
