package cn.asany.storage.data.service;

import cn.asany.storage.data.domain.FileLabel;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileOptions {
  private String storePath;
  @Builder.Default private Boolean hidden = Boolean.FALSE;
  private Set<FileLabel> labels;
}
