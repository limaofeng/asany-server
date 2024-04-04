package cn.asany.cms.module;

import cn.asany.base.IModuleProperties;
import cn.asany.cms.module.dto.ArticleChannelImpObj;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 内容管理模块
 *
 * @author limaofeng
 */
@Data
@NoArgsConstructor
public class CmsModuleProperties implements IModuleProperties {
  @Setter private String type;
  @Setter private String rootCategory;
  private List<ArticleChannelImpObj> channels;

  @Override
  public String getType() {
    return "cms";
  }
}
