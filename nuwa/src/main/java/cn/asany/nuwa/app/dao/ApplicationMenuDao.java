package cn.asany.nuwa.app.dao;

import cn.asany.nuwa.app.domain.ApplicationMenu;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ApplicationMenu
 *
 * @author limaofeng
 */
@Repository
public interface ApplicationMenuDao extends JpaRepository<ApplicationMenu, Long> {}
