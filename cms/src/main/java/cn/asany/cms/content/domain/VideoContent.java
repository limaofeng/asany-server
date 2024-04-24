package cn.asany.cms.content.domain;

import cn.asany.cms.article.domain.ArticleContent;
import cn.asany.cms.content.domain.enums.ContentType;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.converter.FileObjectConverter;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

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
  @TableGenerator
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
