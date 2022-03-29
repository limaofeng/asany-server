package cn.asany.storage.data.convert;

import cn.asany.storage.api.MultipartUploadOptions;
import cn.asany.storage.data.graphql.input.MultipartUploadInput;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface MultipartUploadOptionsConverter {

  @Mappings({
    @Mapping(source = "metadata.size", target = "size"),
    @Mapping(source = "metadata.mimeType", target = "mimeType")
  })
  MultipartUploadOptions toOptions(MultipartUploadInput input);
}
