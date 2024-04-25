/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.cms.article.service;

import cn.asany.cms.article.dao.ArticleCategoryDao;
import cn.asany.cms.article.dao.ArticleDao;
import cn.asany.cms.article.domain.Article;
import cn.asany.cms.article.domain.ArticleCategory;
import cn.asany.cms.article.domain.PageComponent;
import cn.asany.cms.article.domain.enums.ArticleStatus;
import cn.asany.cms.article.event.ArticleCategoryPageUpdateEvent;
import cn.asany.ui.resources.domain.Component;
import cn.asany.ui.resources.domain.enums.ComponentScope;
import cn.asany.ui.resources.service.ComponentService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.util.PinyinUtils;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.framework.util.common.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 文章栏目服务
 *
 * @author limaofeng
 */
@Slf4j
@Service
@Transactional
public class ArticleCategoryService {

  private final ArticleDao articleDao;
  private final ArticleCategoryDao categoryDao;
  private final ComponentService componentService;
  private final ApplicationContext applicationContext;

  @Autowired
  public ArticleCategoryService(
      ApplicationContext applicationContext,
      ArticleCategoryDao categoryDao,
      ArticleDao articleDao,
      ComponentService componentService) {
    this.categoryDao = categoryDao;
    this.articleDao = articleDao;
    this.componentService = componentService;
    this.applicationContext = applicationContext;
  }

  public Page<ArticleCategory> findPage(Pageable pageable, PropertyFilter filter) {
    return this.categoryDao.findPage(pageable, filter);
  }

  public Optional<ArticleCategory> findById(Long id) {
    return categoryDao.findById(id);
  }

  public Optional<ArticleCategory> findUniqueByName(String name) {
    return categoryDao.findOne(Example.of(ArticleCategory.builder().name(name).build()));
  }

  public List<ArticleCategory> findAll(PropertyFilter filter) {
    return this.categoryDao.findAll(filter);
  }

  public List<ArticleCategory> findAllArticle(PropertyFilter filter, Sort orderBy) {
    return this.categoryDao.findAll(filter, orderBy);
  }

  public List<ArticleCategory> findAll(ArticleCategory ArticleCategory, Sort orderBy) {
    return this.categoryDao.findAll(Example.of(ArticleCategory), orderBy);
  }

  /**
   * 批量保存
   *
   * @param channels 栏目
   * @return List<ArticleChannel>
   */
  public List<ArticleCategory> saveAll(List<ArticleCategory> channels, Long rootChannelId) {

    ArticleCategory rootChannel = this.categoryDao.getReferenceById(rootChannelId);

    channels.forEach(
        item -> {
          item.setOrganization(rootChannel.getOrganization());
          item.setParent(rootChannel);
        });

    List<Article> articles = new ArrayList<>();
    channels =
        ObjectUtil.recursive(
            channels,
            (item, context) -> {
              int index = context.getIndex();
              int level = context.getLevel();

              if (context.getParent() != null) {
                item.setOrganization(context.getParent().getOrganization());
              }

              // 暂存文章
              if (item.getArticles() != null) {
                articles.addAll(
                    item.getArticles().stream()
                        .peek(
                            article -> {
                              article.setCategory(item);
                              article.setOrganization(rootChannel.getOrganization());
                              article.setStatus(ArticleStatus.PUBLISHED);
                            })
                        .collect(Collectors.toList()));
              }

              Optional<ArticleCategory> optional =
                  this.categoryDao.findOne(
                      PropertyFilter.newFilter()
                          .equal("slug", item.getSlug())
                          .startsWith("path", rootChannel.getPath()));

              if (optional.isPresent()) {
                item.setId(optional.get().getId());
                item.setArticles(optional.get().getArticles());
              } else {
                this.categoryDao.save(item);
              }

              ArticleCategory parent =
                  ObjectUtil.defaultValue(context.getParent(), item.getParent());
              if (parent == null) {
                item.setPath(item.getId() + ArticleCategory.SEPARATOR);
              } else {
                item.setPath(parent.getPath() + item.getId() + ArticleCategory.SEPARATOR);
              }
              item.setParent(parent);
              item.setIndex(index);
              item.setLevel(level);
              item.setOrganization(rootChannel.getOrganization());

              return item;
            });
    channels = ObjectUtil.flat(channels, "children");
    this.categoryDao.updateAllInBatch(channels);

    this.articleDao.saveAll(articles);
    return channels;
  }

  public ArticleCategory save(String name) {
    ArticleCategory channel = new ArticleCategory();
    channel.setName(name);
    channel = this.categoryDao.save(channel);
    channel.setPath(
        channel.getParent() == null
            ? channel.getId() + ArticleCategory.SEPARATOR
            : channel.getParent().getPath() + channel.getId() + ArticleCategory.SEPARATOR);
    return channel;
  }

  /**
   * 保存栏目
   *
   * @param channel 频道
   * @return ArticleChannel
   */
  public ArticleCategory save(ArticleCategory channel) {
    Integer index = channel.getIndex();

    boolean isRoot = channel.getParent() == null;

    ArticleCategory parent =
        isRoot ? null : this.categoryDao.getReferenceById(channel.getParent().getId());

    if (StringUtil.isBlank(channel.getSlug())) {
      channel.setSlug(pinyin(channel.getName()));
    }

    if (isRoot) {
      channel.setLevel(1);
    } else {
      channel.setLevel(parent.getLevel() + 1);
    }

    channel = categoryDao.save(channel);

    List<ArticleCategory> _siblings = siblings(channel.getParent(), channel);
    if (_siblings.isEmpty()) {
      channel.setIndex(0);
    } else if (index == null) {
      channel.setIndex(_siblings.get(_siblings.size() - 1).getIndex() + 1);
    } else {
      int maxIndex = _siblings.get(_siblings.size() - 1).getIndex() + 1;
      index = Math.min(maxIndex, index);
      channel.setIndex(index);
      rearrange(_siblings, index, Integer.MAX_VALUE, true);
    }
    if (isRoot) {
      channel.setPath(channel.getId() + ArticleCategory.SEPARATOR);
    } else {
      channel.setPath(parent.getPath() + channel.getId() + ArticleCategory.SEPARATOR);
    }
    return this.categoryDao.update(channel);
  }

  /**
   * 修改栏目
   *
   * @param id ID
   * @param merge 合并
   * @param category 栏目
   * @return ArticleChannel
   */
  public ArticleCategory update(Long id, ArticleCategory category, boolean merge) {
    category.setId(id);
    ArticleCategory prev = this.categoryDao.getReferenceById(id);

    int sourceIndex = prev.getIndex();
    Integer index = category.getIndex();

    ArticleCategory sourceParent = prev.getParent();
    ArticleCategory parent = category.getParent();

    boolean notRoot = parent != null && !parent.getId().equals(0L);
    if (notRoot) {
      parent = categoryDao.getReferenceById(parent.getId());
    }

    boolean _isChangeParent = isChangeParent(parent, sourceParent);

    category.setParent(sourceParent);
    category.setIndex(sourceIndex);

    if (savePage(category, prev)) {
      applicationContext.publishEvent(
          new ArticleCategoryPageUpdateEvent(category, category.getPage()));
    }

    category = categoryDao.update(category, merge);

    if (_isChangeParent) {
      boolean isRoot = parent.getId().equals(0L);

      rearrange(siblings(sourceParent, category), sourceIndex, Integer.MAX_VALUE, false);

      List<ArticleCategory> _siblings = siblings(parent, category);
      int maxIndex = _siblings.get(_siblings.size() - 1).getIndex() + 1;
      category.setParent(isRoot ? null : parent);
      category.setLevel(isRoot ? 0 : parent.getLevel() + 1);
      if (isRoot) {
        category.setPath(category.getId() + ArticleCategory.SEPARATOR);
      } else {
        category.setPath(parent.getPath() + category.getId() + ArticleCategory.SEPARATOR);
      }

      if (_siblings.isEmpty()) {
        category.setIndex(0);
      } else if (index == null) {
        category.setIndex(maxIndex);
      } else {
        index = Math.min(maxIndex, index);
        category.setIndex(index);
        rearrange(_siblings, index, Integer.MAX_VALUE, true);
      }
    } else if (index != null && sourceIndex != index) {
      List<ArticleCategory> _siblings = siblings(sourceParent, category);
      int maxIndex = _siblings.get(_siblings.size() - 1).getIndex() + 1;
      index = Math.min(maxIndex, index);

      int startIndex = Math.min(sourceIndex, index);
      int endIndex = Math.max(sourceIndex, index);

      category.setIndex(index);
      rearrange(_siblings, startIndex, endIndex, sourceIndex > index);
    }

    return this.categoryDao.update(category);
  }

  private boolean savePage(ArticleCategory category, ArticleCategory prev) {
    PageComponent prevPage = prev.getPage();
    PageComponent page = category.getPage();

    if (page == null) {
      return false;
    }

    boolean isUpdate = !prevPage.getEnabled().equals(page.getEnabled());

    if (isUpdate) {
      prevPage.setEnabled(page.getEnabled());
      prevPage.getRoute().setEnabled(page.getEnabled());
    }

    if (!page.getEnabled()) {
      if (prevPage.getRoute() != null) {
        page.setRoute(prevPage.getRoute());
        page.setComponent(prevPage.getComponent());
      }
      return isUpdate;
    }
    if (prevPage.getComponent() == null) {
      Component newComponent = category.getPage().getComponent();
      newComponent.setScope(ComponentScope.ROUTE);
      newComponent.setName(category.getName());
      page.setComponent(componentService.save(newComponent));
      isUpdate = true;
    } else {
      if (!prevPage.getComponent().getName().equals(category.getName())) {
        isUpdate = true;
      }
      prevPage.getComponent().setName(category.getName());
      if (page.getComponent().getTemplate() != null
          && !prevPage.getComponent().getTemplate().equals(page.getComponent().getTemplate())) {
        isUpdate = true;
        prevPage.getComponent().setTemplate(page.getComponent().getTemplate());
      }
      if (page.getComponent().getBlocks() != null) {
        isUpdate = true;
        prevPage.getComponent().setBlocks(page.getComponent().getBlocks());
      }
      page.setComponent(prevPage.getComponent());
    }
    if (prevPage.getRoute() != null) {
      if (!prevPage.getRoute().getPath().equals(page.getRoute().getPath())) {
        prevPage.getRoute().setPath(page.getRoute().getPath());
        isUpdate = true;
      }
      page.setRoute(prevPage.getRoute());
    } else if (page.getRoute() != null) {
      isUpdate = true;
    }
    return isUpdate;
  }

  private boolean isChangeParent(ArticleCategory currentParent, ArticleCategory sourceParent) {
    if (currentParent == null) {
      return false;
    }
    currentParent = currentParent.getId().equals(0L) ? null : currentParent;
    return currentParent != sourceParent;
  }

  private List<ArticleCategory> siblings(ArticleCategory parent, ArticleCategory current) {
    PropertyFilter filter = PropertyFilter.newFilter().notEqual("id", current.getId());
    if (parent == null || parent.getId().equals(0L)) {
      filter.isNull("parent");
    } else {
      filter.equal("parent.id", parent.getId());
    }
    return ObjectUtil.sort(categoryDao.findAll(filter), "index", "asc");
  }

  /**
   * 重新排序
   *
   * @param channels 兄弟节点（不包含当前节点）
   * @param startIndex 开始位置
   * @param endIndex 结束位置
   * @param back true ? index + 1 : index -1;
   */
  public void rearrange(
      List<ArticleCategory> channels, int startIndex, int endIndex, boolean back) {
    List<ArticleCategory> neighbors =
        channels.stream()
            .filter(item -> item.getIndex() >= startIndex && item.getIndex() <= endIndex)
            .collect(Collectors.toList());
    for (ArticleCategory item : neighbors) {
      item.setIndex(item.getIndex() + (back ? 1 : -1));
    }
    this.categoryDao.updateAllInBatch(neighbors);
  }

  private String pinyin(String name) {
    return PinyinUtils.getAll(name, "-");
  }

  private String generateCode(Long channelId, String code, String name) {
    String slug = StringUtil.defaultValue(code, pinyin(name));
    Optional<ArticleCategory> prev = categoryDao.findOneBy("slug", slug);
    if (!prev.isPresent() || prev.get().getId().equals(channelId)) {
      return code;
    }
    return generateCode(channelId, slug + "-0", name);
  }

  /**
   * * 删除栏目
   *
   * @param id ID
   */
  public boolean delete(Long id) {
    ArticleCategory channel = this.findOne(id);
    if (channel == null) {
      return true;
    } else if (CollectionUtils.isNotEmpty(channel.getChildren())) {
      return false;
    }
    this.categoryDao.deleteById(id);
    return true;
  }

  public void deleteAll() {
    this.articleDao.deleteAll();
    this.categoryDao.deleteAll();
  }

  public ArticleCategory findOne(Long id) {
    return categoryDao.findById(id).orElse(null);
  }

  public Optional<ArticleCategory> findOneBySlug(String slug) {
    return this.categoryDao.findOneBy("slug", slug);
  }

  public ArticleCategory getById(Long id) {
    return this.categoryDao.getReferenceById(id);
  }

  public void clearAll(Long id) {
    ArticleCategory category = this.categoryDao.getReferenceById(id);
    // 删除栏目下的文章
    List<Article> articles =
        this.articleDao.findAll(
            PropertyFilter.newFilter().startsWith("category.path", category.getPath()));
    this.articleDao.deleteAll(articles);
    // 删除栏目
    this.categoryDao.deleteAllByPath(category.getPath(), category.getId());
  }
}
