package cn.asany.sms.dao;

import cn.asany.sms.domain.Provider;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 短信供应商
 *
 * @author limaofeng
 */
@Repository
public interface ProviderDao extends JpaRepository<Provider, String> {}
