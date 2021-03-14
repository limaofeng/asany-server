package cn.asany.shanhai.core.dao;

import cn.asany.shanhai.core.bean.ModelField;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelFieldDao extends JpaRepository<ModelField, Long> {

}
