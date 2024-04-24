package cn.asany.sms.dao;

import cn.asany.sms.domain.Captcha;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptchaDao extends AnyJpaRepository<Captcha, String> {}
