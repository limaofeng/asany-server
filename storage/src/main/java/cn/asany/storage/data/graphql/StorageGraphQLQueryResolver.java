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
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;
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

  public FileObject file(String key) {
    IdUtils.FileKey fileKey = IdUtils.parseKey(key);
    return fileKey.getFile().toFileObject(fileKey.getSpace());
  }

  public FileObjectConnection listFiles(
      String key, FileFilter filter, int page, int pageSize, OrderBy orderBy) {
    IdUtils.FileKey fileKey = IdUtils.parseKey(key);

    PropertyFilterBuilder filterBuilder =
        ObjectUtil.defaultValue(filter, FileFilter::new).getBuilder();

    filterBuilder
        .equal("hidden", false)
        .notEqual("id", fileKey.getRootFolder().getId())
        .startsWith("path", fileKey.getRootFolder().getPath());

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
        (item) ->
            FileObjectConnection.FileObjectEdge.builder()
                .node(item.toFileObject(fileKey.getSpace()))
                .build());
  }
}
