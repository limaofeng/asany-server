package cn.asany.email.domainlist.component;

import cn.asany.email.domainlist.domain.JamesDomain;
import cn.asany.email.domainlist.service.DomainService;
import com.github.steveash.guavate.Guavate;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.james.core.Domain;
import org.apache.james.dnsservice.api.DNSService;
import org.apache.james.domainlist.api.DomainListException;
import org.apache.james.domainlist.lib.AbstractDomainList;
import org.springframework.stereotype.Component;

/**
 * Domain List
 *
 * @author limaofeng
 */
@Slf4j
@Component
public class JamesDomainList extends AbstractDomainList {

  private final DomainService domainService;

  @Inject
  public JamesDomainList(DNSService dns, DomainService domainService) {
    super(dns);
    this.domainService = domainService;
  }

  @Override
  protected List<Domain> getDomainListInternal() throws DomainListException {
    try {
      List<String> resultList = domainService.listDomainNames();
      return resultList.stream().map(Domain::of).collect(Guavate.toImmutableList());
    } catch (PersistenceException e) {
      log.error("Failed to list domains", e);
      throw new DomainListException("Unable to retrieve domains", e);
    }
  }

  @Override
  public Domain getDefaultDomain() throws DomainListException {
    return Domain.of("asany.cn");
  }

  @Override
  protected boolean containsDomainInternal(Domain domain) throws DomainListException {
    try {
      Optional<JamesDomain> optional = this.domainService.findDomainByName(domain.asString());
      return optional.isPresent();
    } catch (PersistenceException e) {
      log.error("Failed to find domain", e);
      throw new DomainListException("Unable to retrieve domains", e);
    }
  }

  @Override
  public void addDomain(Domain domain) throws DomainListException {
    try {
      if (containsDomainInternal(domain)) {
        throw new DomainListException(domain.name() + " already exists.");
      }
      this.domainService.save(new JamesDomain(domain));
    } catch (PersistenceException e) {
      log.error("Failed to save domain", e);
      throw new DomainListException("Unable to add domain " + domain.name(), e);
    }
  }

  @Override
  public void removeDomain(Domain domain) throws DomainListException {
    try {
      if (!containsDomainInternal(domain)) {
        throw new DomainListException(domain.name() + " was not found.");
      }
      this.domainService.deleteDomainByName(domain.asString());
    } catch (PersistenceException e) {
      log.error("Failed to remove domain", e);
      throw new DomainListException("Unable to remove domain " + domain.name(), e);
    }
  }
}
