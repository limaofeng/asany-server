package cn.asany.shanhai.core.dao;

import cn.asany.shanhai.core.domain.ModelFieldArgument;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelFieldArgumentDao extends JpaRepository<ModelFieldArgument, Long> {}
