package cn.asany.sms.dao;

import cn.asany.sms.domain.Provider;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 短信供应商
 *
 * @author limaofeng
 */
@Repository
public interface ProviderDao extends AnyJpaRepository<Provider, String> {}
