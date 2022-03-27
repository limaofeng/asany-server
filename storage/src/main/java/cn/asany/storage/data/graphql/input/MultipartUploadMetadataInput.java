package cn.asany.storage.data.graphql.input;

import lombok.Data;

@Data
public class MultipartUploadMetadataInput {
  private long contentLength;
  private String contentType;
}
