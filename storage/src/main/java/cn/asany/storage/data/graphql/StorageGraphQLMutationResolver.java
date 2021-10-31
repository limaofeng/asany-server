package cn.asany.storage.data.graphql;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.UploadOptions;
import cn.asany.storage.api.UploadService;
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

  public StorageGraphQLMutationResolver(UploadService uploadService) {
    this.uploadService = uploadService;
  }

  public FileObject upload(Part part, UploadOptions options, DataFetchingEnvironment env)
      throws IOException {
    FileObject object = UploadUtils.partToObject(part);
    return uploadService.upload(object, options);
  }
}
