package cn.asany.cms.article.service;

import cn.asany.cms.article.bean.Content;
import cn.asany.cms.article.bean.HtmlContent;
import cn.asany.cms.article.dao.HtmlContentDao;
import org.springframework.stereotype.Service;

/**
 * 内容服务
 *
 * @author limaofeng
 */
@Service
public class ContentService {

  private final HtmlContentDao htmlContentDao;

  public ContentService(HtmlContentDao htmlContentDao) {
    this.htmlContentDao = htmlContentDao;
  }

  public Content save(Content content) {
    if (!(content instanceof HtmlContent)) {
      return null;
    }
    return htmlContentDao.save((HtmlContent) content);
  }

  public Content update(Content content) {
    return htmlContentDao.update((HtmlContent) content, true);
  }
}
