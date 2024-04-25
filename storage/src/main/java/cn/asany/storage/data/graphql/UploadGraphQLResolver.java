/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.storage.data.graphql;

import cn.asany.storage.api.*;
import cn.asany.storage.data.convert.MultipartUploadOptionsConverter;
import cn.asany.storage.data.domain.MultipartUpload;
import cn.asany.storage.data.graphql.input.MultipartUploadInput;
import cn.asany.storage.data.graphql.type.FileChecksum;
import cn.asany.storage.data.service.MultipartUploadService;
import cn.asany.storage.data.util.IdUtils;
import cn.asany.storage.utils.UploadUtils;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import jakarta.servlet.http.Part;
import java.io.IOException;
import net.asany.jfantasy.framework.util.common.StringUtil;
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
    FileObject object = UploadUtils.partToObject(part);
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
