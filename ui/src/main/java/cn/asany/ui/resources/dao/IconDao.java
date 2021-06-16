package cn.asany.ui.resources.dao;

import cn.asany.ui.resources.bean.Icon;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IconDao extends JpaRepository<Icon, Long> {
}
