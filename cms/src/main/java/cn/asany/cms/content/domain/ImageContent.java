package cn.asany.cms.content.domain;

import cn.asany.cms.article.domain.ArticleContent;
import cn.asany.cms.content.domain.enums.ContentType;
import jakarta.persistence.*;
import java.util.List;
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
@Table(name = "CMS_IMAGE_CONTENT")
public class ImageContent extends BaseBusEntity implements ArticleContent {

  @Id
  @Column(name = "ID", nullable = false)
  @TableGenerator
  private Long id;

  /** 图片 */
  @OneToMany(
      mappedBy = "imageContent",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  @OrderBy("index ASC")
  private List<ImageItem> images;

  @Override
  public String generateSummary() {
    return null;
  }

  @Override
  public ContentType getContentType() {
    return ContentType.IMAGE;
  }
}
