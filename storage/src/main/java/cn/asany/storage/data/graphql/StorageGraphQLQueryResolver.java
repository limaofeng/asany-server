package cn.asany.storage.data.graphql;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.bean.StorageConfig;
import cn.asany.storage.data.service.StorageService;
import cn.asany.storage.dto.SimpleFileObject;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * 存储器
 *
 * @author limaofeng
 */
@Component
public class StorageGraphQLQueryResolver implements GraphQLQueryResolver {

  private final StorageService storageService;

  public StorageGraphQLQueryResolver(StorageService storageService) {
    this.storageService = storageService;
  }

  public List<StorageConfig> storages() {
    return this.storageService.findAll();
  }

  public Optional<StorageConfig> storage(String id) {
    return this.storageService.findById(id);
  }

  public List<FileObject> listFiles(String id, String path) {
    return this.storageService.listFiles(id, path).stream()
        .map(FileDetail::toFileObject)
        .collect(Collectors.toList());
  }

  public FileObject file(String id, String path) {
    if (FileObject.ROOT_PATH.equals(path)) {
      return SimpleFileObject.builder().id(-1L).path("/").directory(true).build();
    }
    Optional<FileDetail> optionalFileDetail = this.storageService.findOneByPath(id, path);
    return optionalFileDetail.map(FileDetail::toFileObject).orElse(null);
  }
}
