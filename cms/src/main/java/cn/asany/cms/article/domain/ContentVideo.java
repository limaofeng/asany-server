package cn.asany.cms.article.domain;

import cn.asany.storage.api.FileObject;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContentVideo {
  private Long id;

  /** 地址 */
  private FileObject video;
}
