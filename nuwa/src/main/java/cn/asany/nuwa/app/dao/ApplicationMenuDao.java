package cn.asany.nuwa.app.dao;

import cn.asany.nuwa.app.domain.ApplicationMenu;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ApplicationMenu
 *
 * @author limaofeng
 */
@Repository
public interface ApplicationMenuDao extends AnyJpaRepository<ApplicationMenu, Long> {}
