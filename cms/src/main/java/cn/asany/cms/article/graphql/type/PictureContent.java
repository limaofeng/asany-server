package cn.asany.cms.article.graphql.type;

import cn.asany.cms.article.bean.Content;
import cn.asany.cms.article.bean.ContentPicture;
import cn.asany.cms.article.bean.enums.ContentType;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import lombok.Data;
import org.jfantasy.framework.jackson.JSON;

@Data
public class PictureContent implements IContent {
  private Long id;
  private ContentType type;
  private List<ContentPicture> pictures;

  public PictureContent(Content content) {
    this.pictures =
        JSON.deserialize(content.getText(), new TypeReference<List<ContentPicture>>() {});
  }
}
