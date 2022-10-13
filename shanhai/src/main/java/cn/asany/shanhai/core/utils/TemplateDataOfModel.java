package cn.asany.shanhai.core.utils;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.domain.ModelField;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.jfantasy.framework.spring.SpringBeanUtils;

@AllArgsConstructor
public class TemplateDataOfModel {
  private Model model;

  public String getClassName() {
    return model.getModule().getCode() + "." + model.getCode();
  }

  public String getCode() {
    return model.getCode();
  }

  public String getName() {
    return model.getName();
  }

  public String getDatabaseTableName() {
    return model.getMetadata().getDatabaseTableName();
  }

  public TemplateDataOfModelField getIdField() {
    ModelUtils modelUtils = SpringBeanUtils.getBeanByType(ModelUtils.class);
    Optional<ModelField> idFieldOptional = modelUtils.getId(model);
    return idFieldOptional.map(TemplateDataOfModelField::new).orElse(null);
  }

  public List<TemplateDataOfModelField> getFields() {
    ModelUtils modelUtils = SpringBeanUtils.getBeanByType(ModelUtils.class);
    return modelUtils.getFields(model).stream()
        .map(TemplateDataOfModelField::new)
        .collect(Collectors.toList());
  }

  public List<TemplateDataOfModelQuery> getQueries() {
    return new ArrayList<>();
  }
}
