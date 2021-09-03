package cn.asany.cms.article.graphql.inputs;

import cn.asany.cms.article.bean.enums.ContentType;
import cn.asany.storage.api.FileObject;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContentInput {
  private ContentType type;
  private String html;
  private String link;
  private List<ContentPictureInput> pictures;
  private FileObject video;
}
