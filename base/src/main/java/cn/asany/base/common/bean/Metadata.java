package cn.asany.base.common.bean;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.converter.FileObjectConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {
  /** 缩略图 */
  @Convert(converter = FileObjectConverter.class)
  @Column(name = "META_THUMB", length = 500)
  private FileObject thumb;
  /** 标题 */
  @Column(name = "META_TITLE", length = 200)
  private String title;
  /** 描述 */
  @Column(name = "META_DESCRIPTION", length = 500)
  private String description;
}
