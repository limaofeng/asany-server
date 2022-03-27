package cn.asany.storage.data.graphql.resolver;

import cn.asany.storage.data.bean.MultipartUpload;
import cn.asany.storage.data.util.IdUtils;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

@Component
public class MultipartUploadGraphQLResolver implements GraphQLResolver<MultipartUpload> {

  public String id(MultipartUpload multipartUpload) {
    return IdUtils.toUploadId(multipartUpload.getId());
  }
}
