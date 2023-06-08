package cn.asany.website.data.service;

import cn.asany.website.data.dao.WebsiteDao;
import cn.asany.website.data.domain.Website;
import java.util.List;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class WebsiteService {

  private final WebsiteDao websiteDao;

  public WebsiteService(WebsiteDao websiteDao) {
    this.websiteDao = websiteDao;
  }

  public List<Website> websites(PropertyFilter filter, Sort sort) {
    return this.websiteDao.findAll(filter, sort);
  }

  public Website save(Website website) {
    if (website.getChannel() == null) {
      // 自动创建栏目
      System.out.println(" Website getChannel ");
    }
    return this.websiteDao.save(website);
  }

  public Website get(Long id) {
    return this.websiteDao.getReferenceById(id);
  }
}
