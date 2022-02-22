package cn.asany.email.user.dao;

import cn.asany.email.user.bean.MailSettings;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailSettingsDao extends JpaRepository<MailSettings, Long> {}
