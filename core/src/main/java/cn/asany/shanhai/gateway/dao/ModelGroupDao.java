package cn.asany.shanhai.gateway.dao;

import cn.asany.shanhai.gateway.bean.ModelGroup;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelGroupDao extends JpaRepository<ModelGroup, Long> {
}