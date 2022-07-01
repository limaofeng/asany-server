package cn.asany.cms.module;

import cn.asany.base.IModule;
import cn.asany.cms.article.converter.ArticleCategoryConverter;
import cn.asany.cms.article.service.ArticleCategoryService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 内容管理模块
 *
 * @author limaofeng
 */
public class CmsModule implements IModule<CmsModuleProperties> {

  @Autowired private ArticleCategoryConverter categoryConverter;
  @Autowired private ArticleCategoryService articleCategoryService;

  @Override
  public String name() {
    return "cms";
  }

  @Override
  public void load(CmsModuleProperties properties) {
    this.articleCategoryService.saveAll(categoryConverter.toChannels(properties.getChannels()), 0L);
  }
}
