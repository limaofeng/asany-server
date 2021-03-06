package cn.asany.cms.article.graphql.input;

import cn.asany.cms.article.domain.enums.BackgroundType;
import cn.asany.storage.api.FileObject;
import lombok.Data;

/**
 * Banner 新增对象
 *
 * @author limaofeng
 */
@Data
public class BannerCreateInput {
  /** 标题 */
  private String name;
  /** 标题 */
  private String title;
  /** 副标题 */
  private String subtitle;
  /** 描述 */
  private String description;
  /** 媒介 */
  private BackgroundType backgroundType;
  /** 媒介 */
  private FileObject background;
  /** 地址 */
  private String url;
  /** 按钮文字 */
  private String buttonText;
}
