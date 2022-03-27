package cn.asany.storage.data.graphql.input;

import lombok.Data;

@Data
public class MultipartUploadInput {
  private String name;
  private String hash;
  private String space;
  private String folder;
  private Long chunkSize;
  private Integer chunkLength;
  private MultipartUploadMetadataInput metadata;
}
