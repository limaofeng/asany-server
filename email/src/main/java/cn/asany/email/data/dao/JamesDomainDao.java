package cn.asany.email.data.dao;

import cn.asany.email.data.bean.JamesDomain;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * James Domain Dao
 *
 * @author limaofeng
 */
@Repository
public interface JamesDomainDao extends JpaRepository<JamesDomain, String> {}
