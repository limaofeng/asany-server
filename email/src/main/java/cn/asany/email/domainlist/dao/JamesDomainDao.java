package cn.asany.email.domainlist.dao;

import cn.asany.email.domainlist.bean.JamesDomain;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * James Domain Dao
 *
 * @author limaofeng
 */
@Repository
public interface JamesDomainDao extends JpaRepository<JamesDomain, String> {}
