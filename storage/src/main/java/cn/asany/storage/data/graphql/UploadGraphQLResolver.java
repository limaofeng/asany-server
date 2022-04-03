package cn.asany.storage.data.graphql;

import cn.asany.storage.api.*;
import cn.asany.storage.data.bean.MultipartUpload;
import cn.asany.storage.data.convert.MultipartUploadOptionsConverter;
import cn.asany.storage.data.graphql.input.MultipartUploadInput;
import cn.asany.storage.data.graphql.type.FileChecksum;
import cn.asany.storage.data.service.MultipartUploadService;
import cn.asany.storage.data.util.IdUtils;
import cn.asany.storage.utils.UploadUtils;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.io.IOException;
import javax.servlet.http.Part;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Component;

@Component
public class UploadGraphQLResolver implements GraphQLMutationResolver, GraphQLQueryResolver {

  private final UploadService uploadService;
  private final MultipartUploadService multipartUploadService;
  private final MultipartUploadOptionsConverter multipartUploadOptionsConverter;

  public UploadGraphQLResolver(
      UploadService uploadService,
      MultipartUploadService multipartUploadService,
      MultipartUploadOptionsConverter multipartUploadOptionsConverter) {
    this.uploadService = uploadService;
    this.multipartUploadService = multipartUploadService;
    this.multipartUploadOptionsConverter = multipartUploadOptionsConverter;
  }

  public FileObject upload(Part part, UploadOptions options) throws IOException {
    String space = options.getSpace();
    FileObject object = UploadUtils.partToObject(part);

    String key = StringUtil.defaultValue(options.getFolder(), () -> IdUtils.toKey("space", space));

    IdUtils.FileKey fileKey = IdUtils.parseKey(key);

    return uploadService.upload(object, options);
  }

  public FileChecksum listMultipartUploads(String md5) {
    return FileChecksum.builder().id(md5).build();
  }

  public MultipartUpload initiateMultipartUpload(MultipartUploadInput input)
      throws UploadException {
    MultipartUploadOptions options = multipartUploadOptionsConverter.toOptions(input);

    String id = uploadService.initiateMultipartUpload(options);

    return this.multipartUploadService.get(IdUtils.parseUploadId(id));
  }

  public FileObject completeMultipartUpload(String key, String name, String folder)
      throws UploadException {

    Long id = IdUtils.parseUploadId(key);

    MultipartUpload multipartUpload = this.multipartUploadService.get(id);

    String folderKey =
        StringUtil.defaultValue(folder, () -> IdUtils.toKey("space", multipartUpload.getSpace()));

    IdUtils.FileKey fileKey = IdUtils.parseKey(folderKey);

    return this.uploadService.completeMultipartUpload(key, name, folder);
  }
}
