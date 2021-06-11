package cn.asany.ui.resources.dao;

import cn.asany.ui.resources.bean.Component;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentDao extends JpaRepository<Component, Long> {
}
