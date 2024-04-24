package cn.asany.email.user.dao;

import cn.asany.email.user.domain.MailSettings;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailSettingsDao extends AnyJpaRepository<MailSettings, String> {}
