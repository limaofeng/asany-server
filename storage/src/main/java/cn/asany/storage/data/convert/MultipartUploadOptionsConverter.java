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

  @Mappings({})
  MultipartUploadOptions toOptions(MultipartUploadInput input);
}
