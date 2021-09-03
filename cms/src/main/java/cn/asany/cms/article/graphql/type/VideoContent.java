package cn.asany.cms.article.graphql.type;

import cn.asany.cms.article.bean.Content;
import cn.asany.cms.article.bean.ContentVideo;
import cn.asany.cms.article.bean.enums.ContentType;
import cn.asany.storage.api.converter.FileObjectConverter;
import lombok.Data;

@Data
public class VideoContent implements IContent {
  private Long id;
  private ContentType type;
  private ContentVideo video;

  public VideoContent(Content content) {
    FileObjectConverter converter = new FileObjectConverter();
    this.video =
        ContentVideo.builder()
            .id(content.getId())
            .video(converter.convertToEntityAttribute(content.getText()))
            .build();
  }
}
