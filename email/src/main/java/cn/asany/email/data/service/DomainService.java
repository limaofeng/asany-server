package cn.asany.email.data.service;

import cn.asany.email.data.bean.JamesDomain;
import cn.asany.email.data.dao.JamesDomainDao;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.stereotype.Service;

/**
 * James Domain 服务
 *
 * @author limaofeng
 */
@Service
public class DomainService {

  private final JamesDomainDao jamesDomainDao;

  public DomainService(JamesDomainDao jamesDomainDao) {
    this.jamesDomainDao = jamesDomainDao;
  }

  public List<String> listDomainNames() {
    return this.jamesDomainDao.findAll().stream()
        .map(JamesDomain::getName)
        .collect(Collectors.toList());
  }

  public Optional<JamesDomain> findDomainByName(String name) {
    return this.jamesDomainDao.findById(name);
  }

  public void save(JamesDomain jamesDomain) {
    this.jamesDomainDao.save(jamesDomain);
  }

  public void deleteDomainByName(String name) {
    this.jamesDomainDao.deleteById(name);
  }

  public JamesDomain getDefaultDomain() {
    return this.jamesDomainDao
        .findOne(PropertyFilter.builder().isNull("organization").build())
        .orElseThrow(() -> new RuntimeException("Default Domain is Null"));
  }
}
