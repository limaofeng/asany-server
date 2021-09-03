package cn.asany.cms.article.bean;

import cn.asany.storage.api.FileObject;
import java.io.Serializable;
import lombok.Data;

/**
 * 网页元数据 用于网页 SEO 优化
 *
 * @author limaofeng
 */
@Data
public class MetaData implements Serializable {
  private FileObject file;
  private String url;
  private String title;
  private String description;
}
