package cn.asany.cms.content.domain;

import cn.asany.cms.article.domain.ArticleContent;
import cn.asany.cms.content.domain.enums.ContentType;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.converter.FileObjectConverter;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "CMS_VIDEO_CONTENT")
public class VideoContent extends BaseBusEntity implements ArticleContent {

  @Id
  @Column(name = "ID", nullable = false)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 外部视频地址 */
  @Column(name = "VIDEO_URL", length = 250)
  private String url;
  /** 本地视频文件 */
  @Column(name = "VIDEO_STORE", columnDefinition = "JSON")
  @Convert(converter = FileObjectConverter.class)
  private FileObject video;
  /** 视频标题 */
  @Column(name = "TITLE", length = 50)
  private String title;
  /** 视频描述 */
  @Column(name = "DESCRIPTION", length = 250)
  private String description;

  @Override
  public String generateSummary() {
    return null;
  }

  @Override
  public ContentType getContentType() {
    return ContentType.VIDEO;
  }
}
