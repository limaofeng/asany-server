package cn.asany.storage.data.service;

import cn.asany.storage.data.bean.FilePart;
import cn.asany.storage.data.dao.FilePartDao;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FilePartService {

  private final FilePartDao filePartDao;

  @Autowired
  public FilePartService(FilePartDao filePartDao) {
    this.filePartDao = filePartDao;
  }

  public void save(
      String path,
      String namespace,
      String entireFileHash,
      String partFileHash,
      Integer total,
      Integer index) {
    if (this.findByPartFileHash(entireFileHash, partFileHash) != null) {
      return;
    }
    FilePart part = new FilePart();
    //        part.setPath(path);
    //        part.setNamespace(namespace);
    //        part.setEntireFileHash(entireFileHash);
    //        part.setPartFileHash(partFileHash);
    //        part.setTotal(total);
    //        part.setIndex(index);
    this.filePartDao.save(part);
  }

  public void delete(String path) {
    this.filePartDao.deleteById(path);
  }

  public List<FilePart> find(String entireFileHash) {
    //        return this.filePartDao.find(new Criterion[]{Restrictions.eq("entireFileHash",
    // entireFileHash)}, "index", "asc");
    return new ArrayList<>();
  }

  public Optional<FilePart> findByPartFileHash(String entireFileHash, String partFileHash) {
    return this.filePartDao.findOne(
        PropertyFilter.builder()
            .equal("entireFileHash", entireFileHash)
            .equal("partFileHash", partFileHash)
            .build());
  }
}
