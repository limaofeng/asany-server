package cn.asany.ui.library.dao;

import cn.asany.ui.library.bean.Library;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryDao extends JpaRepository<Library, Long> {
}
