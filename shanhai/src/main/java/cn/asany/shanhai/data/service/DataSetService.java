package cn.asany.shanhai.data.service;

import cn.asany.shanhai.data.dao.DataSetDao;
import cn.asany.shanhai.data.domain.DataSetConfig;
import java.util.Arrays;
import org.springframework.stereotype.Service;

/**
 * 数据集
 *
 * @author limaofeng
 */
@Service
public class DataSetService {
  private final DataSetDao dataSetDao;

  public DataSetService(DataSetDao dataSetDao) {
    this.dataSetDao = dataSetDao;
  }

  public DataSetConfig save(DataSetConfig config) {
    return this.dataSetDao.save(config);
  }

  public DataSetConfig update(Long id, DataSetConfig config, Boolean merge) {
    config.setId(id);
    return this.dataSetDao.update(config, merge);
  }

  public int delete(Long... ids) {
    this.dataSetDao.deleteAllById(Arrays.asList(ids));
    return ids.length;
  }

  public DataSetConfig getConfig(Long id) {
    return this.dataSetDao.getReferenceById(id);
  }
}
