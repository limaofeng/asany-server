package cn.asany.cms.article.domain;

import cn.asany.storage.api.FileObject;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

/**
 * 网页元数据 用于网页 SEO 优化
 *
 * @author limaofeng
 */
@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ArticleMetadata implements Serializable {
  @Type(type = "file")
  @Column(name = "METADATA_IMAGE", length = 500)
  private FileObject image;

  @Column(name = "METADATA_URL", length = 100)
  private String url;

  @Column(name = "METADATA_TITLE", length = 120)
  private String title;

  @Column(name = "METADATA_DESCRIPTION", length = 300)
  private String description;
}
