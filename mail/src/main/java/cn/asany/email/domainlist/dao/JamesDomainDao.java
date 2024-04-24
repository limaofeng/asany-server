package cn.asany.email.domainlist.dao;

import cn.asany.email.domainlist.domain.JamesDomain;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * James Domain Dao
 *
 * @author limaofeng
 */
@Repository
public interface JamesDomainDao extends AnyJpaRepository<JamesDomain, String> {}
