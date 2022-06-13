package cn.asany.cms.article.domain;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.converter.FileObjectConverter;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
  @Convert(converter = FileObjectConverter.class)
  @Column(name = "METADATA_IMAGE", length = 500)
  private FileObject image;

  @Column(name = "METADATA_URL", length = 100)
  private String url;

  @Column(name = "METADATA_TITLE", length = 120)
  private String title;

  @Column(name = "METADATA_DESCRIPTION", length = 300)
  private String description;
}
