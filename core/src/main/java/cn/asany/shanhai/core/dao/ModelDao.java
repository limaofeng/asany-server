package cn.asany.shanhai.core.dao;

import cn.asany.shanhai.core.bean.Model;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelDao extends JpaRepository<Model, Long> {

}
