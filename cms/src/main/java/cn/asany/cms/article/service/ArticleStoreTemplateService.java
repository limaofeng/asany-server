package cn.asany.cms.article.service;

import cn.asany.cms.article.dao.ArticleStoreTemplateDao;
import cn.asany.cms.article.domain.ArticleStoreTemplate;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ArticleStoreTemplateService {

  private final ArticleStoreTemplateDao articleStoreTemplateDao;

  public ArticleStoreTemplateService(ArticleStoreTemplateDao articleStoreTemplateDao) {
    this.articleStoreTemplateDao = articleStoreTemplateDao;
  }

  public List<ArticleStoreTemplate> storeTemplates() {
    return this.articleStoreTemplateDao.findAll(Sort.by("index").ascending());
  }

  public Optional<ArticleStoreTemplate> findById(Long id) {
    return this.articleStoreTemplateDao.findById(id);
  }
}
