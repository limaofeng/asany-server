package cn.asany.shanhai.core.support.model.features;

import cn.asany.shanhai.core.domain.ModelField;
import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.core.support.model.FieldType;
import cn.asany.shanhai.core.support.model.IModelFeature;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.springframework.stereotype.Component;

/**
 * 系统字段
 *
 * @author limaofeng
 */
@Data
@Component
public class SystemFieldsFeature implements IModelFeature {
  public static final String ID = "system-fields";
  private String id = ID;

  private final ModelService modelService;

  public SystemFieldsFeature(ModelService modelService) {
    this.modelService = modelService;
  }

  @Override
  public List<ModelField> fields() {
    List<ModelField> fields = new ArrayList<>();
    fields.add(
        ModelField.builder()
            .code(BaseBusEntity.FIELD_CREATED_BY)
            .name("创建人")
            .system(true)
            .sort(2)
            .metadata(true, false, false)
            .type(FieldType.Int)
            .build());
    fields.add(
        ModelField.builder()
            .code(BaseBusEntity.FIELD_CREATED_AT)
            .name("创建时间")
            .system(true)
            .sort(1)
            .metadata(true, false)
            .type(FieldType.Date)
            .build());
    fields.add(
        ModelField.builder()
            .code(BaseBusEntity.FIELD_UPDATED_BY)
            .name("修改人")
            .system(true)
            .sort(4)
            .metadata(true, false, false)
            .type(FieldType.Int)
            .build());
    fields.add(
        ModelField.builder()
            .code(BaseBusEntity.FIELD_UPDATED_AT)
            .name("修改时间")
            .system(true)
            .sort(3)
            .type(FieldType.Date)
            .build());
    return fields;
  }
}
