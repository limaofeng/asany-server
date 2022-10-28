package cn.asany.shanhai.view.dao;

import cn.asany.shanhai.view.domain.ModelView;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelViewDao extends JpaRepository<ModelView, Long> {}
