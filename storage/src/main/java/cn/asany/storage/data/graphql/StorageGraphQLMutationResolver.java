package cn.asany.storage.data.graphql;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.UploadOptions;
import cn.asany.storage.api.UploadService;
import cn.asany.storage.data.service.StorageService;
import cn.asany.storage.utils.UploadUtils;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import java.io.IOException;
import javax.servlet.http.Part;
import org.springframework.stereotype.Component;

/**
 * 文件上传
 *
 * @author limaofeng
 */
@Component
public class StorageGraphQLMutationResolver implements GraphQLMutationResolver {

  private final UploadService uploadService;
  private final StorageService storageService;

  public StorageGraphQLMutationResolver(
      UploadService uploadService, StorageService storageService) {
    this.uploadService = uploadService;
    this.storageService = storageService;
  }

  public Boolean storageReindex(String id) {
    this.storageService.reindex(id);
    return true;
  }

  public FileObject upload(Part part, UploadOptions options, DataFetchingEnvironment env)
      throws IOException {
    FileObject object = UploadUtils.partToObject(part);
    return uploadService.upload(object, options);
  }
}
