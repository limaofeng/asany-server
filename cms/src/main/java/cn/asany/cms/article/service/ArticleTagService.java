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

import cn.asany.cms.article.dao.ArticleTagDao;
import cn.asany.cms.article.domain.ArticleTag;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.util.common.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class ArticleTagService {

  private static final String PATH_SEPARATOR = "/";
  private final ArticleTagDao tagDao;

  @Autowired
  public ArticleTagService(ArticleTagDao tagDao) {
    this.tagDao = tagDao;
  }

  public Page<ArticleTag> findPage(Pageable pageable, PropertyFilter filter) {
    return this.tagDao.findPage(pageable, filter);
  }

  public Optional<ArticleTag> get(Long id) {
    return tagDao.findById(id);
  }

  public Optional<ArticleTag> get(String code) {
    return tagDao.findOne(Example.of(ArticleTag.builder().slug(code).build()));
  }

  public Optional<ArticleTag> findUniqueByName(String name) {
    return tagDao.findOne(Example.of(ArticleTag.builder().name(name).build()));
  }

  public List<ArticleTag> findAll(PropertyFilter filter) {
    return this.tagDao.findAll(filter);
  }

  public List<ArticleTag> findAllArticle(PropertyFilter filter, Sort orderBy) {
    return this.tagDao.findAll(filter, orderBy);
  }

  public List<ArticleTag> findAll(ArticleTag articleTag, Sort orderBy) {
    return this.tagDao.findAll(Example.of(articleTag), orderBy);
  }

  public ArticleTag update(ArticleTag tag, boolean patch) {
    return this.tagDao.update(tag, patch);
  }

  public ArticleTag save(String name) {
    ArticleTag tag = new ArticleTag();
    //        try {
    //            tag.setCode(PinyinUtils.getAll(name, "-"));
    //        } catch (PinyinException e) {
    //            Log.error(e.getMessage(), e);
    //            tag.setCode(name);
    //        }
    tag.setName(name);
    tag = this.tagDao.save(tag);
    tag.setPath(
        tag.getParent() == null
            ? tag.getId() + PATH_SEPARATOR
            : tag.getParent().getPath() + tag.getId() + PATH_SEPARATOR);
    return tag;
  }

  /**
   * 保存栏目
   *
   * @param tag 标签
   * @return ArticleTag
   */
  public ArticleTag save(ArticleTag tag) {
    tag.setSlug(
        generateCode(
            tag.getId(),
            StringUtil.defaultValue(tag.getSlug(), pinyin(tag.getName())),
            tag.getName()));
    tag = tagDao.save(tag);
    tag.setPath(
        tag.getParent() == null
            ? tag.getId() + PATH_SEPARATOR
            : tag.getParent().getPath() + tag.getId() + PATH_SEPARATOR);
    return tag;
  }

  // 修改栏目
  public ArticleTag update(Long tagId, boolean merge, ArticleTag tag) {
    tag.setId(tagId);
    tag.setSlug(
        generateCode(
            tag.getId(),
            StringUtil.defaultValue(tag.getSlug(), pinyin(tag.getName())),
            tag.getName()));
    tag = tagDao.update(tag, merge);
    tag.setPath(
        tag.getParent() == null
            ? tag.getId() + PATH_SEPARATOR
            : tag.getParent().getPath() + tag.getId() + PATH_SEPARATOR);
    return tag;
  }

  private String pinyin(String name) {
    //        try {
    //            return PinyinUtils.getAll(name, "-");
    //        } catch (PinyinException e) {
    //            Log.error(e.getMessage(), e);
    //            return null;
    //        }
    return null;
  }

  private String generateCode(Long tagId, String code, String name) {
    Optional<ArticleTag> prevTag =
        tagDao.findOne(Example.of(ArticleTag.builder().slug(code).build()));
    if (!prevTag.isPresent() || prevTag.get().getId().equals(tagId)) {
      return code;
    }
    return generateCode(tagId, code + "-0", name);
  }

  /**
   * * 删除栏目
   *
   * @param id ID
   */
  public boolean delete(Long id) {
    ArticleTag tag = this.findOne(id);
    if (tag == null) {
      return true;
    } else if (CollectionUtils.isNotEmpty(tag.getChildren())) {
      return false;
    }
    tagDao.deleteArticleChannel(id);
    tagDao.deleteArticleTags(id);
    this.tagDao.deleteById(id);
    return true;
  }

  public ArticleTag findOne(Long parent) {
    return tagDao.findById(parent).orElse(null);
  }
}
