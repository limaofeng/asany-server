package cn.asany.shanhai.core.convert;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.domain.ModelFeature;
import cn.asany.shanhai.core.domain.ModelField;
import cn.asany.shanhai.core.graphql.inputs.ModelCreateInput;
import cn.asany.shanhai.core.graphql.inputs.ModelFieldInput;
import cn.asany.shanhai.core.graphql.inputs.ModelUpdateInput;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * 项目 转换器
 *
 * @author limaofeng
 */
@Mapper(
    componentModel = "spring",
    builder = @Builder(disableBuilder = true),
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class ModelConverter {

  /**
   * 将 ModelCreateInput 转换为 Model
   *
   * @param input 模型
   * @return Model
   */
  @Mappings({
    @Mapping(source = "databaseTableName", target = "metadata.databaseTableName"),
    @Mapping(source = "module", target = "module.id"),
    @Mapping(source = "fields", target = "fields", qualifiedByName = "toFields"),
    @Mapping(source = "features", target = "features", qualifiedByName = "toFeatures"),
  })
  public abstract Model toModel(ModelCreateInput input);

  @Named("toFields")
  Set<ModelField> toFields(Set<ModelFieldInput> fields) {
    if (fields == null) {
      return new HashSet<>();
    }
    return fields.stream().map(this::toField).collect(Collectors.toSet());
  }

  @Mappings({
    @Mapping(source = "type", target = "type"),
    @Mapping(source = "databaseColumnName", target = "metadata.databaseColumnName"),
  })
  public abstract ModelField toField(ModelFieldInput input);

  @Named("toFeatures")
  Set<ModelFeature> toFeatures(Set<String> features) {
    return features.stream()
        .map(item -> ModelFeature.builder().id(item).build())
        .collect(Collectors.toSet());
  }

  /**
   * 将 ModelUpdateInput 转换为 Model
   *
   * @param input 模型
   * @return Model
   */
  @Mappings({
    @Mapping(source = "databaseTableName", target = "metadata.databaseTableName"),
    @Mapping(source = "fields", target = "fields", qualifiedByName = "toFields"),
    @Mapping(source = "features", target = "features", qualifiedByName = "toFeatures"),
  })
  public abstract Model toModel(ModelUpdateInput input);
}
