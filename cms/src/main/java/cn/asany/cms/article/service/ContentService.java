package cn.asany.cms.article.service;

import cn.asany.cms.article.bean.Content;
import cn.asany.cms.article.dao.ContentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContentService {

  @Autowired private ContentDao contentDao;

  public Content save(Content content) {
    return contentDao.save(content);
  }

  public Content update(Content content) {
    return contentDao.update(content, true);
  }
}
