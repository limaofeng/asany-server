package cn.asany.cms.article.service;

import cn.asany.cms.article.bean.ArticleChannel;
import cn.asany.cms.article.dao.ArticleChannelDao;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ArticleChannelService {

  private static final Log Log = LogFactory.getLog(ArticleChannelService.class);
  private static final String PATH_SEPARATOR = "/";
  private final ArticleChannelDao channelDao;

  @Autowired
  public ArticleChannelService(ArticleChannelDao channelDao) {
    this.channelDao = channelDao;
  }

  public Pager<ArticleChannel> findPager(
      Pager<ArticleChannel> pager, List<PropertyFilter> filters) {
    return this.channelDao.findPager(pager, filters);
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
    channels =
        ObjectUtil.recursive(
            channels,
            (item, context) -> {
              int index = context.getIndex();
              int level = context.getLevel();
              ArticleChannel parent = context.getParent();
              if (parent == null) {
                item.setPath(item.getId() + "/");
              } else {
                item.setPath(parent.getPath() + item.getId() + "/");
              }
              item.setParent(parent);
              item.setIndex(index);
              item.setLevel(level);
              return item;
            });
    channels = ObjectUtil.flat(channels, "children");
    this.channelDao.saveAllInBatch(channels);
    return channels;
  }

  public ArticleChannel save(String name) {
    ArticleChannel channel = new ArticleChannel();
    channel.setName(name);
    channel = this.channelDao.save(channel);
    channel.setPath(
        channel.getParent() == null
            ? channel.getId() + PATH_SEPARATOR
            : channel.getParent().getPath() + channel.getId() + PATH_SEPARATOR);
    return channel;
  }

  /**
   * 保存栏目
   *
   * @param channel
   * @return
   */
  public ArticleChannel save(ArticleChannel channel) {
    channel.setSlug(
        generateCode(
            channel.getId(),
            StringUtil.defaultValue(channel.getSlug(), pinyin(channel.getName())),
            channel.getName()));
    channel = channelDao.save(channel);
    channel.setPath(
        channel.getParent() == null
            ? channel.getId() + PATH_SEPARATOR
            : channel.getParent().getPath() + channel.getId() + PATH_SEPARATOR);
    return channel;
  }

  // 修改栏目
  public ArticleChannel update(Long channelId, boolean merge, ArticleChannel channel) {
    channel.setId(channelId);
    channel.setSlug(
        generateCode(
            channel.getId(),
            StringUtil.defaultValue(channel.getSlug(), pinyin(channel.getName())),
            channel.getName()));
    channel = channelDao.update(channel, merge);
    channel.setPath(
        channel.getParent() == null
            ? channel.getId() + PATH_SEPARATOR
            : channel.getParent().getPath() + channel.getId() + PATH_SEPARATOR);
    return channel;
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

  private String generateCode(Long channelId, String code, String name) {
    Optional<ArticleChannel> prevchannel =
        channelDao.findOne(Example.of(ArticleChannel.builder().slug(code).build()));
    if (!prevchannel.isPresent() || prevchannel.get().getId().equals(channelId)) {
      return code;
    }
    return generateCode(channelId, code + "-0", name);
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

  public ArticleChannel findOne(Long parent) {
    return channelDao.findById(parent).orElse(null);
  }
}
