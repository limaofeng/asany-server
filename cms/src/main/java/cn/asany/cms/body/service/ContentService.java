package cn.asany.cms.body.service;

import cn.asany.cms.article.domain.ArticleBody;
import cn.asany.cms.article.domain.enums.ArticleBodyType;
import cn.asany.cms.body.dao.ContentDao;
import cn.asany.cms.body.domain.Content;
import org.springframework.stereotype.Service;

/**
 * 内容服务
 *
 * @author limaofeng
 */
@Service
public class ContentService implements ArticleBodyHandler {

  private final ContentDao contentDao;

  public ContentService(ContentDao contentDao) {
    this.contentDao = contentDao;
  }

  @Override
  public boolean supports(ArticleBodyType type) {
    return type.name().equals(Content.TYPE_KEY);
  }

  public ArticleBody save(ArticleBody articleBody) {
    if (!(articleBody instanceof Content)) {
      return null;
    }
    return contentDao.save((Content) articleBody);
  }

  @Override
  public ArticleBody update(Long id, ArticleBody body) {
    body.setId(id);
    return contentDao.update((Content) body);
  }

  @Override
  public void delete(Long id) {
    this.contentDao.deleteById(id);
  }

  public ArticleBody update(ArticleBody articleBody) {
    return contentDao.update((Content) articleBody, true);
  }
}
