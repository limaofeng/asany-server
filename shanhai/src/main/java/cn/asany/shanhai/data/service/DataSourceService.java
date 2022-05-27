package cn.asany.shanhai.data.service;

import cn.asany.shanhai.data.dao.DataSourceDao;
import cn.asany.shanhai.data.domain.DataSourceConfig;
import java.util.Arrays;
import org.springframework.stereotype.Service;

/**
 * 数据源服务
 *
 * @author limaofeng
 */
@Service
public class DataSourceService {

  private final DataSourceDao dataSourceDao;

  public DataSourceService(DataSourceDao dataSourceDao) {
    this.dataSourceDao = dataSourceDao;
  }

  public DataSourceConfig save(DataSourceConfig config) {
    return this.dataSourceDao.save(config);
  }

  public DataSourceConfig update(Long id, DataSourceConfig config, Boolean merge) {
    config.setId(id);
    return this.dataSourceDao.update(config, merge);
  }

  public int delete(Long... ids) {
    this.dataSourceDao.deleteAllById(Arrays.asList(ids));
    return ids.length;
  }

  public DataSourceConfig getConfig(Long id) {
    return this.dataSourceDao.getReferenceById(id);
  }
}
