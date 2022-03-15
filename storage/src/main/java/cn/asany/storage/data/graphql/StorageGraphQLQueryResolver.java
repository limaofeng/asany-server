package cn.asany.storage.data.graphql;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.bean.StorageConfig;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.data.service.SpaceService;
import cn.asany.storage.data.service.StorageService;
import cn.asany.storage.data.util.IdUtils;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jfantasy.graphql.context.AuthorizationGraphQLServletContext;
import org.springframework.stereotype.Component;

/**
 * 存储器
 *
 * @author limaofeng
 */
@Component
public class StorageGraphQLQueryResolver implements GraphQLQueryResolver {

  private final StorageService storageService;

  public StorageGraphQLQueryResolver(
      FileService fileService, SpaceService spaceService, StorageService storageService) {
    this.storageService = storageService;
  }

  public List<StorageConfig> storages() {
    return this.storageService.findAll();
  }

  public Optional<StorageConfig> storage(String id) {
    return this.storageService.findById(id);
  }

  public FileObject file(String key, DataFetchingEnvironment environment) {
    IdUtils.FileKey fileKey = IdUtils.parseKey(key);

    AuthorizationGraphQLServletContext context = environment.getContext();

    context.setAttribute("QUERY_ROOT_FILE_KEY", fileKey);
    context.setAttribute("QUERY_ROOT_PATH", fileKey.getRootPath());

    Optional<FileDetail> optionalFileDetail =
        this.storageService.findOneByPath(fileKey.getStorage(), fileKey.getPath());
    return optionalFileDetail.map(FileDetail::toFileObject).orElse(null);
  }

  public List<FileObject> listFiles(String key, DataFetchingEnvironment environment) {
    IdUtils.FileKey fileKey = IdUtils.parseKey(key);

    AuthorizationGraphQLServletContext context = environment.getContext();

    context.setAttribute("QUERY_ROOT_FILE_KEY", fileKey);

    return this.storageService.listFiles(fileKey.getStorage(), fileKey.getPath()).stream()
        .map(FileDetail::toFileObject)
        .collect(Collectors.toList());
  }
}
