package cn.asany.storage.data.graphql;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.UploadOptions;
import cn.asany.storage.api.UploadService;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.utils.UploadUtils;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import java.io.IOException;
import javax.servlet.http.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author limaofeng */
@Component
public class StorageGraphQLMutationResolver implements GraphQLMutationResolver {

  @Autowired private UploadService uploadService;

  public FileDetail upload(Part part, UploadOptions options, DataFetchingEnvironment env)
      throws IOException {
    FileObject object = UploadUtils.partToObject(part);
    return (FileDetail) uploadService.upload(object, options);
  }
}
