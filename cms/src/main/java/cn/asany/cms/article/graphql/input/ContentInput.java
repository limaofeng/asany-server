package cn.asany.cms.article.graphql.input;

import cn.asany.cms.content.domain.enums.TextContentType;
import cn.asany.storage.api.FileObject;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 输入内容
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentInput {
  private TextContentType type;
  private String text;
  private String link;
  private List<ContentPictureInput> pictures;
  private FileObject video;
}
