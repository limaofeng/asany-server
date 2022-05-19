package cn.asany.cms.article.service;

import cn.asany.cms.article.bean.Article;
import cn.asany.cms.article.bean.ArticleChannel;
import cn.asany.cms.article.bean.enums.ArticleCategory;
import cn.asany.cms.article.bean.enums.ArticleStatus;
import cn.asany.cms.article.bean.enums.ArticleType;
import cn.asany.cms.article.dao.ArticleChannelDao;
import cn.asany.cms.article.dao.ArticleDao;
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
public class ArticleChannelService {

  private final ArticleDao articleDao;
  private final ArticleChannelDao channelDao;

  @Autowired
  public ArticleChannelService(ArticleChannelDao channelDao, ArticleDao articleDao) {
    this.channelDao = channelDao;
    this.articleDao = articleDao;
  }

  public Page<ArticleChannel> findPage(Pageable pageable, List<PropertyFilter> filters) {
    return this.channelDao.findPage(pageable, filters);
  }

  public Optional<ArticleChannel> get(Long id) {
    return channelDao.findById(id);
  }

  public Optional<ArticleChannel> get(String code) {
    return channelDao.findOne(Example.of(ArticleChannel.builder().slug(code).build()));
  }

  public Optional<ArticleChannel> findUniqueByName(String name) {
    return channelDao.findOne(Example.of(ArticleChannel.builder().name(name).build()));
  }

  public List<ArticleChannel> findAll(List<PropertyFilter> filters) {
    return this.channelDao.findAll(filters);
  }

  public List<ArticleChannel> findAllArticle(List<PropertyFilter> filters, Sort sort) {
    return this.channelDao.findAll(filters, sort);
  }

  public List<ArticleChannel> findAll(ArticleChannel ArticleChannel, Sort sort) {
    return this.channelDao.findAll(Example.of(ArticleChannel), sort);
  }

  public ArticleChannel update(ArticleChannel channel, boolean patch) {
    return this.channelDao.update(channel, patch);
  }

  /**
   * 批量保存
   *
   * @param channels 栏目
   * @return List<ArticleChannel>
   */
  public List<ArticleChannel> saveAll(List<ArticleChannel> channels) {
    List<Article> articles = new ArrayList<>();
    channels =
        ObjectUtil.recursive(
            channels,
            (item, context) -> {
              int index = context.getIndex();
              int level = context.getLevel();

              // 暂存文章
              if (item.getArticles() != null) {
                articles.addAll(
                    item.getArticles().stream()
                        .map(
                            article -> {
                              article.setChannels(new ArrayList<>());
                              article.getChannels().add(item);
                              article.setType(ArticleType.text);
                              article.setCategory(ArticleCategory.news);
                              article.setStatus(ArticleStatus.PUBLISHED);
                              return article;
                            })
                        .collect(Collectors.toList()));
              }

              Optional<ArticleChannel> optional = this.channelDao.findOneBy("slug", item.getSlug());

              if (optional.isPresent()) {
                item.setId(optional.get().getId());
                item.setArticles(optional.get().getArticles());
              } else {
                this.channelDao.save(item);
              }

              ArticleChannel parent = context.getParent();
              if (parent == null) {
                item.setPath(item.getId() + ArticleChannel.SEPARATOR);
              } else {
                item.setPath(parent.getPath() + item.getId() + ArticleChannel.SEPARATOR);
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

  public ArticleChannel save(String name) {
    ArticleChannel channel = new ArticleChannel();
    channel.setName(name);
    channel = this.channelDao.save(channel);
    channel.setPath(
        channel.getParent() == null
            ? channel.getId() + ArticleChannel.SEPARATOR
            : channel.getParent().getPath() + channel.getId() + ArticleChannel.SEPARATOR);
    return channel;
  }

  /**
   * 保存栏目
   *
   * @param channel
   * @return
   */
  public ArticleChannel save(ArticleChannel channel) {
    Integer index = channel.getIndex();

    boolean isRoot = channel.getParent() == null;

    ArticleChannel parent = isRoot ? this.channelDao.getById(channel.getParent().getId()) : null;

    if (StringUtil.isBlank(channel.getSlug())) {
      channel.setSlug(pinyin(channel.getName()));
    }

    if (isRoot) {
      channel.setLevel(1);
    } else {
      channel.setLevel(parent.getLevel() + 1);
    }

    channel = channelDao.save(channel);

    List<ArticleChannel> _siblings = siblings(channel.getParent(), channel);
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
      channel.setPath(channel.getId() + ArticleChannel.SEPARATOR);
    } else {
      channel.setPath(parent.getPath() + channel.getId() + ArticleChannel.SEPARATOR);
    }
    return this.channelDao.update(channel);
  }

  /**
   * 修改栏目
   *
   * @param id
   * @param merge
   * @param channel
   * @return
   */
  public ArticleChannel update(Long id, boolean merge, ArticleChannel channel) {
    channel.setId(id);
    ArticleChannel prev = this.channelDao.getById(id);

    int sourceIndex = prev.getIndex();
    Integer index = channel.getIndex();

    ArticleChannel sourceParent = prev.getParent();
    ArticleChannel parent = channel.getParent();

    boolean notRoot = parent != null && !parent.getId().equals(0L);
    if (notRoot) {
      parent = channelDao.getById(parent.getId());
    }

    boolean _isChangeParent = isChangeParent(parent, sourceParent);

    channel.setParent(sourceParent);
    channel.setIndex(sourceIndex);

    channel = channelDao.update(channel, merge);

    if (_isChangeParent) {
      boolean isRoot = parent.getId().equals(0L);

      rearrange(siblings(sourceParent, channel), sourceIndex, Integer.MAX_VALUE, false);

      List<ArticleChannel> _siblings = siblings(parent, channel);
      int maxIndex = _siblings.get(_siblings.size() - 1).getIndex() + 1;
      channel.setParent(isRoot ? null : parent);
      channel.setLevel(isRoot ? 0 : parent.getLevel() + 1);
      if (isRoot) {
        channel.setPath(channel.getId() + ArticleChannel.SEPARATOR);
      } else {
        channel.setPath(parent.getPath() + channel.getId() + ArticleChannel.SEPARATOR);
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
      List<ArticleChannel> _siblings = siblings(sourceParent, channel);
      int maxIndex = _siblings.get(_siblings.size() - 1).getIndex() + 1;
      index = Math.min(maxIndex, index);

      int startIndex = Math.min(sourceIndex, index);
      int endIndex = Math.max(sourceIndex, index);

      channel.setIndex(index);
      rearrange(_siblings, startIndex, endIndex, sourceIndex > index);
    }
    return this.channelDao.update(channel);
  }

  private boolean isChangeParent(ArticleChannel currentParent, ArticleChannel sourceParent) {
    if (currentParent == null) {
      return false;
    }
    currentParent = currentParent.getId().equals(0L) ? null : currentParent;
    return currentParent != sourceParent;
  }

  private List<ArticleChannel> siblings(ArticleChannel parent, ArticleChannel current) {
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
  public void rearrange(List<ArticleChannel> channels, int startIndex, int endIndex, boolean back) {
    List<ArticleChannel> neighbors =
        channels.stream()
            .filter(item -> item.getIndex() >= startIndex && item.getIndex() <= endIndex)
            .collect(Collectors.toList());
    for (ArticleChannel item : neighbors) {
      item.setIndex(item.getIndex() + (back ? 1 : -1));
    }
    this.channelDao.updateAllInBatch(neighbors);
  }

  private String pinyin(String name) {
    return PinyinUtils.getAll(name, "-");
  }

  private String generateCode(Long channelId, String code, String name) {
    String slug = StringUtil.defaultValue(code, pinyin(name));
    Optional<ArticleChannel> prev = channelDao.findOneBy("slug", slug);
    if (!prev.isPresent() || prev.get().getId().equals(channelId)) {
      return code;
    }
    return generateCode(channelId, slug + "-0", name);
  }

  /**
   * * 删除栏目
   *
   * @param id
   */
  public boolean delete(Long id) {
    ArticleChannel channel = this.findOne(id);
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

  public ArticleChannel findOne(Long id) {
    return channelDao.findById(id).orElse(null);
  }

  public Optional<ArticleChannel> findOneBySlug(String slug) {
    return this.channelDao.findOneBy("slug", slug);
  }
}
