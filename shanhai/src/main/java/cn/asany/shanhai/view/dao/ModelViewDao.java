package cn.asany.shanhai.view.dao;

import cn.asany.shanhai.view.domain.ModelView;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelViewDao extends AnyJpaRepository<ModelView, Long> {}
