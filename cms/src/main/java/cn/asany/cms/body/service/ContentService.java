package cn.asany.cms.body.service;

import cn.asany.cms.article.domain.ArticleBody;
import cn.asany.cms.body.dao.ContentDao;
import cn.asany.cms.body.domain.Content;
import org.springframework.stereotype.Service;

/**
 * 内容服务
 *
 * @author limaofeng
 */
@Service
public class ContentService {

  private final ContentDao contentDao;

  public ContentService(ContentDao contentDao) {
    this.contentDao = contentDao;
  }

  public ArticleBody save(ArticleBody articleBody) {
    if (!(articleBody instanceof Content)) {
      return null;
    }
    return contentDao.save((Content) articleBody);
  }

  public ArticleBody update(ArticleBody articleBody) {
    return contentDao.update((Content) articleBody, true);
  }
}
