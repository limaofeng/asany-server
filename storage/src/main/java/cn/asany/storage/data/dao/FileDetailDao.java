package cn.asany.storage.data.dao;

import cn.asany.storage.data.bean.FileDetail;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDetailDao extends JpaRepository<FileDetail, Long> {}
