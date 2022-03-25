package cn.asany.storage.data.graphql;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.bean.StorageConfig;
import cn.asany.storage.data.graphql.input.FileFilter;
import cn.asany.storage.data.graphql.type.FileObjectConnection;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.data.service.SpaceService;
import cn.asany.storage.data.service.StorageService;
import cn.asany.storage.data.util.IdUtils;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.context.AuthorizationGraphQLServletContext;
import org.jfantasy.graphql.util.Kit;
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

  public FileObjectConnection listFiles(
      String key,
      FileFilter filter,
      int page,
      int pageSize,
      OrderBy orderBy,
      DataFetchingEnvironment environment) {
    IdUtils.FileKey fileKey = IdUtils.parseKey(key);

    AuthorizationGraphQLServletContext context = environment.getContext();

    context.setAttribute("QUERY_ROOT_FILE_KEY", fileKey);

    PropertyFilterBuilder filterBuilder =
        ObjectUtil.defaultValue(filter, FileFilter::new).getBuilder();

    if (filter.isRecursive()) {
      filterBuilder.startsWith("path", fileKey.getPath()).notEqual("path", fileKey.getPath());
    } else {
      filterBuilder.equal("parentFile.path", fileKey.getPath());
    }

    if (!fileKey.getPath().contains(FileDetail.NAME_OF_THE_RECYCLE_BIN)) {
      filterBuilder.notStartsWith(
          "path",
          fileKey.getRootPath() + FileDetail.NAME_OF_THE_RECYCLE_BIN + FileObject.SEPARATOR);
    }

    filterBuilder.equal("storageConfig.id", fileKey.getStorage());

    Pager<FileDetail> pager =
        this.storageService.findPager(
            Pager.newPager(
                page,
                pageSize,
                ObjectUtil.defaultValue(
                    orderBy, () -> OrderBy.by(OrderBy.desc("isDirectory"), OrderBy.asc("name")))),
            filterBuilder.build());

    return Kit.connection(
        pager,
        FileObjectConnection.class,
        (item) -> FileObjectConnection.FileObjectEdge.builder().node(item.toFileObject()).build());
  }
}
