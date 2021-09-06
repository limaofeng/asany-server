package cn.asany.cms.article.service;

import cn.asany.cms.article.bean.Content;
import cn.asany.cms.article.bean.HtmlContent;
import cn.asany.cms.article.dao.HtmlContentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContentService {

  @Autowired private HtmlContentDao htmlContentDao;

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
