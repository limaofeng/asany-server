package cn.asany.shanhai.core.convert;

import cn.asany.shanhai.core.domain.Module;
import cn.asany.shanhai.core.graphql.inputs.ModuleCreateInput;
import cn.asany.shanhai.core.graphql.inputs.ModuleUpdateInput;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    builder = @Builder(disableBuilder = true),
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ModuleConverter {

  @Mappings({})
  Module toModule(ModuleCreateInput input);

  @Mappings({})
  Module toModule(ModuleUpdateInput input);
}
