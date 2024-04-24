package cn.asany.storage.data.graphql;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.data.domain.FileDetail;
import cn.asany.storage.data.domain.StorageConfig;
import cn.asany.storage.data.graphql.input.FileWhereInput;
import cn.asany.storage.data.graphql.type.FileObjectConnection;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.data.service.SpaceService;
import cn.asany.storage.data.service.StorageService;
import cn.asany.storage.data.util.IdUtils;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.graphql.util.Kit;
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
      String key, FileWhereInput where, int pageNumber, int pageSize, Sort orderBy) {
    IdUtils.FileKey fileKey = IdUtils.parseKey(key);

    PropertyFilter filter = ObjectUtil.defaultValue(where, FileWhereInput::new).toFilter();

    filter
        .notEqual("id", fileKey.getRootFolder().getId())
        .startsWith("path", fileKey.getRootFolder().getPath());

    if (!fileKey.getFile().isRecycleBin()) {
      filter.equal("hidden", false);
    }

    Page<FileDetail> page =
        this.storageService.findPage(PageRequest.of(pageNumber - 1, pageSize, orderBy), filter);
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
