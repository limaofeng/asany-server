package cn.asany.sms.dao;

import cn.asany.sms.domain.Captcha;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptchaDao extends JpaRepository<Captcha, String> {}
