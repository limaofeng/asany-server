package cn.asany.storage.data.dao;

import cn.asany.storage.data.bean.Folder;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderDao extends JpaRepository<Folder, Long> {}
