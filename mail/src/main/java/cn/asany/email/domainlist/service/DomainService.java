package cn.asany.email.domainlist.service;

import cn.asany.email.domainlist.dao.JamesDomainDao;
import cn.asany.email.domainlist.domain.JamesDomain;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.cache.annotation.Cacheable;
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

  @Cacheable(key = "targetClass + '.' + methodName + '#' + #p0", value = "MAIL_DOMAIN")
  public Optional<JamesDomain> findDomainByName(String name) {
    return this.jamesDomainDao.findById(name);
  }

  public void save(JamesDomain jamesDomain) {
    this.jamesDomainDao.save(jamesDomain);
  }

  public void deleteDomainByName(String name) {
    this.jamesDomainDao.deleteById(name);
  }

  @Cacheable(key = "targetClass + methodName", value = "MAIL_DOMAIN")
  public JamesDomain getDefaultDomain() {
    return this.jamesDomainDao
        .findOne(PropertyFilter.newFilter().isNull("organization"))
        .orElseThrow(() -> new RuntimeException("Default Domain is Null"));
  }
}
