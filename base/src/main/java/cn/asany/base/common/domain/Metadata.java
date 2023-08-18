package cn.asany.base.common.domain;

import cn.asany.storage.api.FileObject;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {
  /** 缩略图 */
  @Type(type = "file")
  @Column(name = "META_THUMB", length = 500)
  private FileObject thumb;
  /** 标题 */
  @Column(name = "META_TITLE", length = 200)
  private String title;
  /** 描述 */
  @Column(name = "META_DESCRIPTION", length = 500)
  private String description;
}
