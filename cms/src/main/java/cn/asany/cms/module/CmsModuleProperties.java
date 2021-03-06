package cn.asany.cms.module;

import cn.asany.base.IModuleProperties;
import cn.asany.cms.module.dto.ArticleChannelImpObj;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 内容管理模块
 *
 * @author limaofeng
 */
@Data
@NoArgsConstructor
public class CmsModuleProperties implements IModuleProperties {
  private String type;
  private List<ArticleChannelImpObj> channels;

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String getType() {
    return "cms";
  }
}
