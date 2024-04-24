package cn.asany.cms.article.service;

import cn.asany.cms.article.dao.ArticleCategoryDao;
import cn.asany.cms.article.dao.ArticleDao;
import cn.asany.cms.article.dao.ArticleMetaFieldDao;
import cn.asany.cms.article.domain.Article;
import cn.asany.cms.article.domain.ArticleCategory;
import cn.asany.cms.article.domain.ArticleContent;
import cn.asany.cms.article.domain.enums.ArticleStatus;
import cn.asany.cms.article.event.ArticleUpdateEvent;
import cn.asany.cms.article.graphql.input.PermissionInput;
import cn.asany.cms.content.service.ArticleContentService;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.util.common.DateUtil;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.framework.util.common.StringUtil;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.*;
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
public class ArticleService {
  private final ApplicationContext applicationContext;
  private final ArticleFeatureService featureService;
  private final ArticleTagService tagService;
  private final ArticleDao articleDao;
  private final ArticleCategoryDao articleCategoryDao;
  private final ArticleContentService contentService;

  private final ArticleMetaFieldDao articleMetaFieldDao;

  @Autowired
  public ArticleService(
      ArticleDao articleDao,
      ApplicationContext applicationContext,
      ArticleFeatureService featureService,
      ArticleTagService tagService,
      ArticleContentService contentService,
      ArticleCategoryDao articleCategoryDao,
      ArticleMetaFieldDao articleMetaFieldDao) {
    this.articleDao = articleDao;
    this.applicationContext = applicationContext;
    this.featureService = featureService;
    this.tagService = tagService;
    this.contentService = contentService;
    this.articleCategoryDao = articleCategoryDao;
    this.articleMetaFieldDao = articleMetaFieldDao;
  }

  public Optional<Article> findUniqueBySlug(String slug) {
    return this.articleDao.findOne(Example.of(Article.builder().slug(slug).build()));
  }

  /**
   * 文章查询方法
   *
   * @param pageable 翻页对象
   * @param filter 筛选条件
   * @return string
   */
  public Page<Article> findPage(Pageable pageable, PropertyFilter filter) {
    if (pageable.getSort().isUnsorted()) {
      pageable =
          PageRequest.of(
              pageable.getPageNumber(),
              pageable.getPageSize(),
              Sort.by("publishedAt").descending());
    }
    return articleDao.findPage(pageable, filter);
  }

  public List<Article> findAll(PropertyFilter filter, int offset, int limit, Sort sort) {
    return articleDao.findAll(filter, offset, limit, sort);
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
  @Transactional(rollbackFor = RuntimeException.class)
  public Article save(Article article, List<PermissionInput> permissions) {
    if (StringUtil.isBlank(article.getSlug())) {
      article.setSlug(null);
    }

    ArticleCategory category =
        this.articleCategoryDao.getReferenceById(article.getCategory().getId());
    article.setCategory(category);

    // 保存正文
    saveContentAndSummary(article);

    if (article.getStatus() == null) {
      article.setStatus(ArticleStatus.DRAFT);
    }

    Article save = this.articleDao.save(article);

    // 判断是否需要审批
    if (ArticleStatus.PUBLISHED == save.getStatus()) {
      publish(article, permissions);
    }

    return article;
  }

  /**
   * 文章发布
   *
   * @param article
   * @param permissions
   */
  public void publish(Article article, List<PermissionInput> permissions) {}

  /**
   * 更新文章
   *
   * @param article 文章
   * @param merge 合并模式
   * @param id ID
   * @return 文章内容
   */
  @Transactional(rollbackFor = RuntimeException.class)
  public Article update(Long id, Article article, boolean merge) {
    article.setId(id);
    if (StringUtil.isBlank(article.getSlug())) {
      article.setSlug(null);
    }

    saveContentAndSummary(article);

    Article oldArticle = this.articleDao.getReferenceById(article.getId());

    if (article.getStatus() == ArticleStatus.PUBLISHED
        && article.getPublishedAt() == null
        && oldArticle.getPublishedAt() == null) {
      article.setPublishedAt(DateUtil.now());
    }

    boolean unPublishForScheduled =
        oldArticle.getStatus() == ArticleStatus.SCHEDULED
            && article.getStatus() == ArticleStatus.DRAFT;

    article = this.articleDao.update(article, merge);

    if (unPublishForScheduled) {
      article.setPublishedAt(null);
    }

    applicationContext.publishEvent(ArticleUpdateEvent.newInstance(article));
    return this.articleDao.getReferenceById(article.getId());
  }

  /**
   * 设置文章摘要，如果文章存在摘要，则跳过
   *
   * @param article 文章
   */
  private void saveContentAndSummary(Article article) {
    ArticleContent content = article.getContent();
    if (StringUtil.isBlank(article.getSummary()) && ObjectUtil.isNotNull(content)) {
      String summary = content.generateSummary();
      article.setSummary(summary);
    }
    ArticleCategory category =
        this.articleCategoryDao.getReferenceById(article.getCategory().getId());
    if (null != article.getId()) {
      // 编辑
      Article oldArticle = this.articleDao.getReferenceById(article.getId());
      if (StringUtil.isBlank(oldArticle.getContentId())) {
        this.contentService.save(content);
      } else if (category.getStoreTemplate().getContentType() == oldArticle.getContentType()) {
        this.contentService.update(oldArticle.getContent().getId(), content);
      } else {
        this.contentService.deleteById(oldArticle.getContentId(), oldArticle.getContentType());
        this.contentService.save(content);
      }
    } else {
      this.contentService.save(content);
    }
    article.setContentId(content.getId());
    article.setContentType(content.getContentType());
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
    article.setStatus(ArticleStatus.PUBLISHED);
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
  public void unPublish(Long id) {
    Article article = this.get(id);
    article.setStatus(ArticleStatus.DRAFT);
    this.articleDao.update(article, true);
  }

  /**
   * 删除文章
   *
   * @param ids 文章 ids
   */
  public long deleteArticle(Long... ids) {
    Set<Article> articles = new HashSet<>();
    for (Long id : ids) {
      Optional<Article> optional = this.articleDao.findById(id);
      if (!optional.isPresent()) {
        continue;
      }
      Article article = optional.get();
      if (article.getContentId() != null) {
        this.contentService.deleteById(article.getContentId(), article.getContentType());
      }
      articles.add(optional.get());
    }
    if (!articles.isEmpty()) {
      this.articleDao.deleteAllById(ObjectUtil.toFieldList(articles, "id", new ArrayList<>()));
    }
    return articles.size();
  }
}
