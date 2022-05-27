package cn.asany.email.user.dao;

import cn.asany.email.user.domain.MailSettings;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailSettingsDao extends JpaRepository<MailSettings, String> {}
