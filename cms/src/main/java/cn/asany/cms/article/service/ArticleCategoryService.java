package cn.asany.cms.article.service;

import cn.asany.cms.article.dao.ArticleCategoryDao;
import cn.asany.cms.article.dao.ArticleDao;
import cn.asany.cms.article.domain.Article;
import cn.asany.cms.article.domain.ArticleCategory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.PinyinUtils;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
  private final ArticleCategoryDao channelDao;

  @Autowired
  public ArticleCategoryService(ArticleCategoryDao channelDao, ArticleDao articleDao) {
    this.channelDao = channelDao;
    this.articleDao = articleDao;
  }

  public Page<ArticleCategory> findPage(Pageable pageable, List<PropertyFilter> filters) {
    return this.channelDao.findPage(pageable, filters);
  }

  public Optional<ArticleCategory> findById(Long id) {
    return channelDao.findById(id);
  }

  public Optional<ArticleCategory> findUniqueByName(String name) {
    return channelDao.findOne(Example.of(ArticleCategory.builder().name(name).build()));
  }

  public List<ArticleCategory> findAll(List<PropertyFilter> filters) {
    return this.channelDao.findAll(filters);
  }

  public List<ArticleCategory> findAllArticle(List<PropertyFilter> filters, Sort orderBy) {
    return this.channelDao.findAll(filters, orderBy);
  }

  public List<ArticleCategory> findAll(ArticleCategory ArticleCategory, Sort orderBy) {
    return this.channelDao.findAll(Example.of(ArticleCategory), orderBy);
  }

  public ArticleCategory update(ArticleCategory channel, boolean patch) {
    return this.channelDao.update(channel, patch);
  }

  /**
   * 批量保存
   *
   * @param channels 栏目
   * @return List<ArticleChannel>
   */
  public List<ArticleCategory> saveAll(List<ArticleCategory> channels, Long rootChannelId) {

    ArticleCategory rootChannel = this.channelDao.getReferenceById(rootChannelId);

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
              //              if (item.getArticles() != null) {
              //                articles.addAll(
              //                    item.getArticles().stream()
              //                        .peek(
              //                            article -> {
              //                              article.setChannels(new ArrayList<>());
              //                              article.getChannels().add(item);
              //                              article.setType(ArticleType.text);
              //                              article.setCategory(ArticleCategory.news);
              //                              article.setStatus(ArticleStatus.PUBLISHED);
              //                            })
              //                        .collect(Collectors.toList()));
              //              }

              Optional<ArticleCategory> optional =
                  this.channelDao.findOne(
                      PropertyFilter.builder()
                          .equal("slug", item.getSlug())
                          .startsWith("path", rootChannel.getPath())
                          .build());

              if (optional.isPresent()) {
                item.setId(optional.get().getId());
                //                item.setArticles(optional.get().getArticles());
              } else {
                this.channelDao.save(item);
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

              return item;
            });
    channels = ObjectUtil.flat(channels, "children");
    this.channelDao.updateAllInBatch(channels);

    this.articleDao.saveAll(articles);
    return channels;
  }

  public ArticleCategory save(String name) {
    ArticleCategory channel = new ArticleCategory();
    channel.setName(name);
    channel = this.channelDao.save(channel);
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
        isRoot ? null : this.channelDao.getReferenceById(channel.getParent().getId());

    if (StringUtil.isBlank(channel.getSlug())) {
      channel.setSlug(pinyin(channel.getName()));
    }

    if (isRoot) {
      channel.setLevel(1);
    } else {
      channel.setLevel(parent.getLevel() + 1);
    }

    channel = channelDao.save(channel);

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
    return this.channelDao.update(channel);
  }

  /**
   * 修改栏目
   *
   * @param id ID
   * @param merge 合并
   * @param channel 栏目
   * @return ArticleChannel
   */
  public ArticleCategory update(Long id, boolean merge, ArticleCategory channel) {
    channel.setId(id);
    ArticleCategory prev = this.channelDao.getReferenceById(id);

    int sourceIndex = prev.getIndex();
    Integer index = channel.getIndex();

    ArticleCategory sourceParent = prev.getParent();
    ArticleCategory parent = channel.getParent();

    boolean notRoot = parent != null && !parent.getId().equals(0L);
    if (notRoot) {
      parent = channelDao.getReferenceById(parent.getId());
    }

    boolean _isChangeParent = isChangeParent(parent, sourceParent);

    channel.setParent(sourceParent);
    channel.setIndex(sourceIndex);

    channel = channelDao.update(channel, merge);

    if (_isChangeParent) {
      boolean isRoot = parent.getId().equals(0L);

      rearrange(siblings(sourceParent, channel), sourceIndex, Integer.MAX_VALUE, false);

      List<ArticleCategory> _siblings = siblings(parent, channel);
      int maxIndex = _siblings.get(_siblings.size() - 1).getIndex() + 1;
      channel.setParent(isRoot ? null : parent);
      channel.setLevel(isRoot ? 0 : parent.getLevel() + 1);
      if (isRoot) {
        channel.setPath(channel.getId() + ArticleCategory.SEPARATOR);
      } else {
        channel.setPath(parent.getPath() + channel.getId() + ArticleCategory.SEPARATOR);
      }

      if (_siblings.isEmpty()) {
        channel.setIndex(0);
      } else if (index == null) {
        channel.setIndex(maxIndex);
      } else {
        index = Math.min(maxIndex, index);
        channel.setIndex(index);
        rearrange(_siblings, index, Integer.MAX_VALUE, true);
      }
    } else if (index != null && sourceIndex != index) {
      List<ArticleCategory> _siblings = siblings(sourceParent, channel);
      int maxIndex = _siblings.get(_siblings.size() - 1).getIndex() + 1;
      index = Math.min(maxIndex, index);

      int startIndex = Math.min(sourceIndex, index);
      int endIndex = Math.max(sourceIndex, index);

      channel.setIndex(index);
      rearrange(_siblings, startIndex, endIndex, sourceIndex > index);
    }
    return this.channelDao.update(channel);
  }

  private boolean isChangeParent(ArticleCategory currentParent, ArticleCategory sourceParent) {
    if (currentParent == null) {
      return false;
    }
    currentParent = currentParent.getId().equals(0L) ? null : currentParent;
    return currentParent != sourceParent;
  }

  private List<ArticleCategory> siblings(ArticleCategory parent, ArticleCategory current) {
    PropertyFilterBuilder builder = PropertyFilter.builder().notEqual("id", current.getId());
    if (parent == null || parent.getId().equals(0L)) {
      builder.isNull("parent");
    } else {
      builder.equal("parent.id", parent.getId());
    }
    return ObjectUtil.sort(channelDao.findAll(builder.build()), "index", "asc");
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
    this.channelDao.updateAllInBatch(neighbors);
  }

  private String pinyin(String name) {
    return PinyinUtils.getAll(name, "-");
  }

  private String generateCode(Long channelId, String code, String name) {
    String slug = StringUtil.defaultValue(code, pinyin(name));
    Optional<ArticleCategory> prev = channelDao.findOneBy("slug", slug);
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
    this.channelDao.deleteById(id);
    return true;
  }

  public void deleteAll() {
    this.articleDao.deleteAll();
    this.channelDao.deleteAll();
  }

  public ArticleCategory findOne(Long id) {
    return channelDao.findById(id).orElse(null);
  }

  public Optional<ArticleCategory> findOneBySlug(String slug) {
    return this.channelDao.findOneBy("slug", slug);
  }
}
