package cn.asany.storage.data.service;

import cn.asany.storage.api.Storage;
import cn.asany.storage.api.StorageBuilder;
import cn.asany.storage.data.domain.StorageConfig;
import cn.asany.storage.data.domain.enums.StorageType;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.spring.SpringBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * FileManager 管理类 1.从数据库。初始化FileManager类
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-7-12 下午03:57:31
 */
@Transactional
public class FileManagerFactory { // implements ApplicationListener<ContextRefreshedEvent>

  private static final Log LOG = LogFactory.getLog(FileManagerFactory.class);

  private static FileManagerFactory fileManagerFactory;

  private StorageService storageService;

  private Map<StorageType, StorageBuilder> builders = new EnumMap<>(StorageType.class);

  private static final ConcurrentMap<String, Storage> fileManagerCache = new ConcurrentHashMap<>();

  public void afterPropertiesSet() throws Exception {
    //        this.addBuilder(StorageType.local, new LocalStorageBuilder());
    //        this.addBuilder(StorageType.oss, new OSSStorageBuilder());
  }

  //    @Async
  //    @Override
  //    public void onApplicationEvent(ContextRefreshedEvent event) {
  //        if (fileManagerCache.isEmpty()) {
  //            this.load();
  //        }
  //    }

  //    private void load() {
  //        long start = System.currentTimeMillis();
  //        // 初始化文件管理器
  //        for (StorageConfig config : fileManagerService.getAll()) {
  //            try {
  //                FileManagerFactory.this.register(config.getId(), config.getType(),
  // config.getConfigParams());
  //            } catch (Exception ex) {
  //                LOG.error("注册 FileManager id = [" + config.getId() + "] 失败!", ex);
  //            }
  //        }
  //        LOG.error("\n初始化 FileManagerFactory 耗时:" + (System.currentTimeMillis() - start) + "ms");
  //    }

  //    public void register(String id, StorageType type, final List<ConfigParam> configParams) {
  //        if (!builders.containsKey(type)) {
  //            LOG.error(" 未找到 [" + type + "] 对应的构建程序!请参考 FileManagerBuilder 实现,并添加到
  // FileManagerFactory 的配置中");
  //            return;
  //        }
  //        Map<String, String> params = new HashMap<>();
  //        for (ConfigParam configParam : configParams) {
  //            params.put(configParam.getName(), configParam.getValue());
  //        }
  //        fileManagerCache.put(id, builders.get(type).register(params));
  //    }

  /**
   * 获取文件管理的单例对象
   *
   * @return FileManagerFactory
   */
  public static synchronized FileManagerFactory getInstance() {
    if (fileManagerFactory == null) {
      fileManagerFactory = SpringBeanUtils.getBeanByType(FileManagerFactory.class);
    }
    return fileManagerFactory;
  }

  /**
   * 根据id获取对应的文件管理器
   *
   * @param id Id
   * @return FileManager
   */
  //    public Storage getFileManager(String id) {
  //        if (fileManagerCache.isEmpty()) {
  //            this.load();
  //        }
  //        return fileManagerCache.get(id);
  //    }
  public void remove(StorageConfig config) {
    fileManagerCache.remove(config.getId());
  }

  public void addBuilder(StorageType type, StorageBuilder builder) {
    this.builders.put(type, builder);
  }

  @Autowired
  public void setFileManagerService(StorageService storageService) {
    this.storageService = storageService;
  }
}
