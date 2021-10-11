package cn.asany.cms.article.service;

import cn.asany.cms.article.bean.Article;
import cn.asany.cms.article.bean.ArticleFeature;
import cn.asany.cms.article.bean.Feature;
import cn.asany.cms.article.bean.HtmlContent;
import cn.asany.cms.article.bean.enums.ArticleFeatureStatus;
import cn.asany.cms.article.bean.enums.ArticleStatus;
import cn.asany.cms.article.dao.ArticleDao;
import cn.asany.cms.article.dao.ArticleFeatureDao;
import cn.asany.cms.article.event.ArticleUpdateEvent;
import cn.asany.cms.article.graphql.input.PermissionInput;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.htmlcleaner.TagNode;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.htmlcleaner.HtmlCleanerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CMS service
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-5-28 下午03:05:30
 */
@Slf4j
@Service
@Transactional
public class ArticleService {
  private final ArticleDao articleDao;

  @Autowired
  public ArticleService(ArticleDao articleDao) {
    this.articleDao = articleDao;
  }

  @Autowired private ApplicationContext applicationContext;
  @Autowired private ArticleFeatureDao articleRecommendDao;
  @Autowired private FeatureService service;
  @Autowired private ArticleTagService tagService;

  public Optional<Article> findUniqueByUrl(String url) {
    return this.articleDao.findOne(Example.of(Article.builder().url(url).build()));
  }

  /**
   * 文章查询方法
   *
   * @param pager  翻页对象
   * @param filters 筛选条件
   * @return string
   */
  public Pager<Article> findPager(Pager<Article> pager, List<PropertyFilter> filters) {
    if (!pager.isOrderBySetted()) {
      pager.sort("publishedAt", OrderBy.Direction.DESC);
    }
    return articleDao.findPager(pager, filters);
  }

  public List<Article> findAll(Article article) {
    return articleDao.findAll(Example.of(article));
  }

  /**
   * 保存文章
   *
   * @param article 文章对象
   * @return Article
   */
  public Article save(Article article, List<PermissionInput> permissions) {
    if (StringUtil.isBlank(article.getUrl())) {
      article.setUrl(null);
    }
    if (log.isDebugEnabled()) {
      log.debug("保存文章 > " + JSON.serialize(article));
    }
    updateContentAndSummary(article);

    if (article.getStatus() == null) {
      article.setStatus(ArticleStatus.draft);
    }

    Article save = this.articleDao.save(article);
    // 添加特征
    saveArticleRecommend(article);
    // 判断是否需要审批
    if (ArticleStatus.published == save.getStatus()) {
      release(article, permissions);
    }
    return save;
  }

  /**
   * 文章发布
   *
   * @param article
   * @param permissions
   */
  public void release(Article article, List<PermissionInput> permissions) {}

  // 更新文章
  public Article update(Article article, boolean merge, Long id) {
    article.setId(id);
    if (StringUtil.isBlank(article.getUrl())) {
      article.setUrl(null);
    }
    if (log.isDebugEnabled()) {
      log.debug("更新文章 > " + JSON.serialize(article));
    }
    Article oldArticle = this.articleDao.getOne(article.getId());
    // 设置发布状态时，设置默认的发布时间
    if (oldArticle.getStatus() != ArticleStatus.published
        && article.getStatus() == ArticleStatus.published
        && oldArticle.getPublishedAt() == null) {
      article.setPublishedAt(DateUtil.now());
    }
    Article result = updateContentAndSummary(this.articleDao.update(article, merge));
    articleRecommendDao.delete(ArticleFeature.builder().article(result).build());
    saveArticleRecommend(article);
    applicationContext.publishEvent(ArticleUpdateEvent.newInstance(article));
    return result;
  }
  /**
   * 保存文章推荐位数据
   *
   * @param article
   */
  private void saveArticleRecommend(Article article) {
    if (article.getFeatures() != null) {
      for (ArticleFeature item : article.getFeatures()) {
        item.setArticle(article);
        Feature feature = service.findById(item.getFeature().getId());
        item.setFeature(feature);
        item.setStatus(
            feature.getNeedReview()
                ? ArticleFeatureStatus.WAIT_REVIEW
                : ArticleFeatureStatus.PASSED);
      }
      articleRecommendDao.saveAll(article.getFeatures());
    }
  }

  public Article update(Article article) {
    return articleDao.update(article, true);
  }

  /**
   * 设置文章摘要，如果文章存在摘要，则跳过
   *
   * @param article 文章
   * @return 文章
   */
  private Article updateContentAndSummary(Article article) {
    if (StringUtil.isBlank(article.getSummary()) && ObjectUtil.isNotNull(article.getContent())) {
      if (article.getContent() instanceof HtmlContent) {
        TagNode node = HtmlCleanerUtil.htmlCleaner(((HtmlContent) article.getContent()).getText());
        String content = node.getText().toString().trim().replace("\n", "");
        String summary = content.substring(0, Math.min(content.length(), 10));
        article.setSummary(summary);
      }
    }
    return article;
  }

  /**
   * 获取文章
   *
   * @param id 文章id
   * @return Article
   */
  public Article get(Long id) {
    Optional<Article> optional = this.articleDao.findById(id);
    if (!optional.isPresent()) {
      return null;
    }
    Article article = optional.get();
    Hibernate.initialize(article.getCategory());
    Hibernate.initialize(article.getContent());
    return article;
  }

  /**
   * 发布文章
   *
   * @param id 文章 ids
   */
  public void publish(Long id) {
    Article article = this.get(id);
    article.setStatus(ArticleStatus.published);
    if (article.getPublishedAt() == null) {
      article.setPublishedAt(DateUtil.now());
    }
    this.articleDao.save(article);
  }

  /**
   * 关闭文章
   *
   * @param id 文章 ids
   */
  public void unpublish(Long id) {
    Article article = this.get(id);
    article.setStatus(ArticleStatus.draft);
    this.articleDao.update(article, true);
  }

  /**
   * 删除文章
   *
   * @param ids 文章 ids
   */
  public void deleteArticle(Long... ids) {
    for (Long id : ids) {
      this.articleDao.deleteById(id);
    }
  }
}
