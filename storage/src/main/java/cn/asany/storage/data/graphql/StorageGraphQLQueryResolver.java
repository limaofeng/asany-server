package cn.asany.storage.data.graphql;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.Storage;
import cn.asany.storage.core.StorageResolver;
import cn.asany.storage.data.bean.StorageConfig;
import cn.asany.storage.data.service.StorageService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Component;

/**
 * 存储器
 *
 * @author limaofeng
 */
@Component
public class StorageGraphQLQueryResolver implements GraphQLQueryResolver {

  private final StorageService storageService;
  private final StorageResolver storageResolver;

  public StorageGraphQLQueryResolver(
      StorageService storageService, StorageResolver storageResolver) {
    this.storageService = storageService;
    this.storageResolver = storageResolver;
  }

  public List<StorageConfig> storages() {
    return this.storageService.findAll();
  }

  public Optional<StorageConfig> storage(String id) {
    return this.storageService.findById(id);
  }

  public List<FileObject> listFiles(String id, String path) {
    Storage storage = this.storageResolver.resolve(id);
    if (StringUtil.isBlank(path)) {
      return storage.listFiles();
    } else {
      return storage.listFiles(path);
    }
  }

  public FileObject file(String id, String path) {
    Storage storage = this.storageResolver.resolve(id);
    return storage.getFileItem(path);
  }
}
