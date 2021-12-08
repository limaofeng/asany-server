package cn.asany.cms.module;

import cn.asany.base.IModule;
import cn.asany.cms.article.service.ArticleChannelService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 内容管理模块
 *
 * @author limaofeng
 */
public class CmsModule implements IModule<CmsModuleProperties> {

  @Autowired private ArticleChannelService articleChannelService;

  @Override
  public String name() {
    return "cms";
  }

  @Override
  public void load(CmsModuleProperties properties) {
    this.articleChannelService.saveAll(properties.getChannels());
  }
}
