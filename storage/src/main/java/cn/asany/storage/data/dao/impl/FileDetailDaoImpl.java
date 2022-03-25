package cn.asany.storage.data.dao.impl;

import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.dao.FileDetailDao;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.jfantasy.framework.error.IgnoreException;

public class FileDetailDaoImpl extends ComplexJpaRepository<FileDetail, Long>
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

    String sql = "DELETE FROM storage_fileobject where path like :startPath and path <> :path";
    query = this.em.createNativeQuery(subTablesql);
    query.setParameter("startPath", path + '%');
    query.setParameter("path", path);

    return query.executeUpdate();
  }

  @Override
  public int replacePath(String storage, String path, String newPath) {
    String sql =
        "UPDATE STORAGE_FILEOBJECT SET PATH = CONCAT(:newPath, SUBSTRING(PATH, :length)) where PATH like :path ";
    Query query = this.em.createNativeQuery(sql);
    query.setParameter("path", path + '%');
    query.setParameter("length", path.length() + 1);
    query.setParameter("newPath", newPath);
    return query.executeUpdate();
  }
}
