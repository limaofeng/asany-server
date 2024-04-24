package cn.asany.shanhai.core.dao;

import cn.asany.shanhai.core.domain.ModelFieldArgument;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelFieldArgumentDao extends AnyJpaRepository<ModelFieldArgument, Long> {}
