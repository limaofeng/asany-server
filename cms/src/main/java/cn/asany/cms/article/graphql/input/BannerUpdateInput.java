package cn.asany.cms.article.graphql.input;

import cn.asany.cms.article.bean.enums.BackgroundType;
import cn.asany.storage.api.FileObject;
import lombok.Data;

/**
 * Banner 修改对象
 *
 * @author limaofeng
 */
@Data
public class BannerUpdateInput {
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
