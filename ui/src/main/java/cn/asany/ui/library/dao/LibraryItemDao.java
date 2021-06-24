package cn.asany.ui.library.dao;

import cn.asany.ui.library.bean.LibraryItem;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryItemDao extends JpaRepository<LibraryItem, Long> {
}
