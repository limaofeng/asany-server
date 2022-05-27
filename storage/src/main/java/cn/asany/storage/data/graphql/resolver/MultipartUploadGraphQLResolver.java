package cn.asany.storage.data.graphql.resolver;

import cn.asany.storage.data.domain.MultipartUpload;
import cn.asany.storage.data.domain.MultipartUploadChunk;
import cn.asany.storage.data.util.IdUtils;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.ArrayList;
import java.util.List;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.stereotype.Component;

@Component
public class MultipartUploadGraphQLResolver implements GraphQLResolver<MultipartUpload> {

  public String id(MultipartUpload multipartUpload) {
    return IdUtils.toUploadId(multipartUpload.getId());
  }

  public List<MultipartUploadChunk> chunks(MultipartUpload multipartUpload) {
    return ObjectUtil.defaultValue(multipartUpload.getChunks(), ArrayList::new);
  }
}
