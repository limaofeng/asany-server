package cn.asany.sms.dao;

import cn.asany.sms.domain.CaptchaConfig;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptchaConfigDao extends JpaRepository<CaptchaConfig, String> {}
