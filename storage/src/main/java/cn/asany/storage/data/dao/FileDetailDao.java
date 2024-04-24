package cn.asany.storage.data.dao;

import cn.asany.storage.data.domain.FileDetail;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDetailDao extends AnyJpaRepository<FileDetail, Long> {

  @Modifying
  @Query(value = "delete FROM FileDetail WHERE path like :path% ")
  void deleteByPath(@Param("path") String path);

  @Modifying
  Integer clearTrash(@Param("path") String path);

  int replacePath(String path, String newPath);

  @Modifying
  Integer hideFiles(String path, boolean hidden);
}
