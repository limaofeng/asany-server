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
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
      String key, FileFilter filter, int _page, int pageSize, Sort orderBy) {
    IdUtils.FileKey fileKey = IdUtils.parseKey(key);

    PropertyFilterBuilder filterBuilder =
        ObjectUtil.defaultValue(filter, FileFilter::new).getBuilder();

    filterBuilder
        .notEqual("id", fileKey.getRootFolder().getId())
        .startsWith("path", fileKey.getRootFolder().getPath());

    if (!fileKey.getFile().isRecycleBin()) {
      filterBuilder.equal("hidden", false);
    }

    Page<FileDetail> page =
        this.storageService.findPage(
            PageRequest.of(_page - 1, pageSize, orderBy), filterBuilder.build());

    return Kit.connection(
        page,
        FileObjectConnection.class,
        (item) ->
            FileObjectConnection.FileObjectEdge.builder()
                .cursor(IdUtils.toKey(fileKey.getType(), fileKey.getSpace().getId(), item.getId()))
                .node(item.toFileObject(fileKey.getSpace()))
                .build());
  }
}
