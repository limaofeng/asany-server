package cn.asany.storage.data.dao.impl;

import cn.asany.storage.data.dao.FileDetailDao;
import cn.asany.storage.data.domain.FileDetail;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;
import net.asany.jfantasy.framework.error.IgnoreException;

@Slf4j
public class FileDetailDaoImpl extends SimpleAnyJpaRepository<FileDetail, Long>
    implements FileDetailDao {

  public FileDetailDaoImpl(EntityManager entityManager) {
    super(FileDetail.class, entityManager);
  }

  @Override
  public void deleteByPath(String path) {
    throw new IgnoreException(" No SQL ");
  }

  @Override
  public Integer clearTrash(String path) {
    String subTablesql =
        "DELETE FROM storage_fileobject_label where file_id in (select id FROM STORAGE_FILEOBJECT WHERE path like :startPath and path <> :path)";

    Query query = this.em.createNativeQuery(subTablesql);
    query.setParameter("startPath", path + '%');
    query.setParameter("path", path);

    int deletedCount = query.executeUpdate();

    log.debug("table storage_fileobject_label has " + deletedCount + " records deleted ");

    String sql = "DELETE FROM storage_fileobject where path like :startPath and path <> :path";
    query = this.em.createNativeQuery(sql);
    query.setParameter("startPath", path + '%');
    query.setParameter("path", path);

    deletedCount = query.executeUpdate();

    log.debug("table storage_fileobject has " + deletedCount + " records deleted ");

    return deletedCount;
  }

  @Override
  public int replacePath(String path, String newPath) {
    String sql =
        "UPDATE STORAGE_FILEOBJECT SET PATH = CONCAT(:newPath, SUBSTRING(PATH, :length)) where PATH like :path ";
    Query query = this.em.createNativeQuery(sql);
    query.setParameter("path", path + '%');
    query.setParameter("length", path.length() + 1);
    query.setParameter("newPath", newPath);
    return query.executeUpdate();
  }

  @Override
  public Integer hideFiles(String path, boolean hidden) {
    String sql = "UPDATE STORAGE_FILEOBJECT SET HIDDEN = :hidden where PATH like :path ";
    Query query = this.em.createNativeQuery(sql);
    query.setParameter("path", path + '%');
    query.setParameter("hidden", hidden);
    return query.executeUpdate();
  }
}
