package cn.asany.storage.data.graphql;

import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.bean.StorageConfig;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.data.service.StorageService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/** @author limaofeng */
@Component
public class StorageGraphQLQueryResolver implements GraphQLQueryResolver {

  private final StorageService storageService;
  private final FileService fileService;

  public StorageGraphQLQueryResolver(StorageService storageService, FileService fileService) {
    this.storageService = storageService;
    this.fileService = fileService;
  }

  public List<StorageConfig> storages() {
    return this.storageService.findAll();
  }

  public Optional<StorageConfig> storage(String id) {
    return this.storageService.findById(id);
  }

  public Optional<FileDetail> file(Long id) {
    return fileService.findById(id);
  }
}
