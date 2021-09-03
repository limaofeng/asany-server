package cn.asany.cms.article.bean;

import cn.asany.storage.api.FileObject;
import lombok.Builder;
import lombok.Data;

/** @author limaofeng */
@Data
@Builder
public class ContentPicture {

  private Long id;

  /** 地址 */
  private FileObject picture;

  /** 摘要 */
  private String digest;
}
