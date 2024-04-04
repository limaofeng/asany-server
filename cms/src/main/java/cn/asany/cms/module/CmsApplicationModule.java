package cn.asany.cms.module;

import cn.asany.base.IApplicationModule;
import cn.asany.base.ModuleConfig;
import cn.asany.cms.article.converter.ArticleCategoryConverter;
import cn.asany.cms.article.domain.ArticleCategory;
import cn.asany.cms.article.service.ArticleCategoryService;
import cn.asany.organization.core.domain.Organization;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
  public Map<String, String> configuration(ModuleConfig<CmsModuleProperties> config) {
    Map<String, String> configs = new HashMap<>();

    CmsModuleProperties properties = config.getProperties();

    Optional<ArticleCategory> categoryOptional =
        articleCategoryService.findOneBySlug(properties.getRootCategory());

    ArticleCategory root =
        categoryOptional
            .map(
                (category) -> {
                  this.articleCategoryService.clearAll(category.getId());
                  return category;
                })
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
        categoryConverter.toChannels(properties.getChannels()), root.getId());

    configs.put("rootCategory", root.getSlug());
    return configs;
  }
}
