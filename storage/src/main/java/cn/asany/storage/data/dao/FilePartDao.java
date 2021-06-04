package cn.asany.storage.data.dao;

import cn.asany.storage.data.bean.FilePart;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilePartDao extends JpaRepository<FilePart,String> {

}
