package cn.asany.cms.module;

import cn.asany.base.IApplicationModule;
import cn.asany.base.ModuleConfig;
import cn.asany.cms.article.converter.ArticleCategoryConverter;
import cn.asany.cms.article.domain.ArticleCategory;
import cn.asany.cms.article.service.ArticleCategoryService;
import cn.asany.organization.core.domain.Organization;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 内容管理模块
 *
 * @author limaofeng
 */
public class CmsApplicationModule implements IApplicationModule<CmsModuleProperties> {

  @Autowired private ArticleCategoryConverter categoryConverter;
  @Autowired private ArticleCategoryService articleCategoryService;

  @Override
  public String name() {
    return "cms";
  }

  @Override
  public Map<String, String> install(ModuleConfig<CmsModuleProperties> config) {
    Map<String, String> configs = new HashMap<>();

    CmsModuleProperties properties = config.getProperties();

    ArticleCategory root =
        articleCategoryService
            .findOneBySlug(properties.getRootCategory())
            .orElseGet(
                () ->
                    this.articleCategoryService.save(
                        ArticleCategory.builder()
                            .slug(properties.getRootCategory())
                            .name(properties.getRootCategory())
                            .organization(
                                Organization.builder().id(config.getDefaultOrganization()).build())
                            .build()));

    this.articleCategoryService.saveAll(
        categoryConverter.toChannels(properties.getCategories()), root.getId());

    configs.put("rootCategory", root.getSlug());
    return configs;
  }

  @Override
  public void uninstall(ModuleConfig<CmsModuleProperties> config) {
    //    CmsModuleProperties properties = config.getProperties();
    //    articleCategoryService
    //        .findOneBySlug(properties.getRootCategory())
    //        .map(
    //            category -> {
    //              this.articleCategoryService.clearAll(category.getId());
    //              return category;
    //            });
  }
}
