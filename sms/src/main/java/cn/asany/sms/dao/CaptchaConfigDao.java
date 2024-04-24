package cn.asany.sms.dao;

import cn.asany.sms.domain.CaptchaConfig;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptchaConfigDao extends AnyJpaRepository<CaptchaConfig, String> {}
