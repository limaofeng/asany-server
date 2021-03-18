package cn.asany.shanhai.core.utils;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.ModelField;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class TemplateDataOfModel {
    private Model model;

    public String getName() {
        return model.getCode();
    }

    public String getDatabaseTableName() {
        return model.getMetadata().getDatabaseTableName();
    }

    public TemplateDataOfModelField getIdField() {
        Optional<ModelField> idFieldOptional = ModelUtils.getId(model);
        return new TemplateDataOfModelField(idFieldOptional.get());
    }

    public List<TemplateDataOfModelField> getFields() {
        return ModelUtils.getFields(model).stream().map(item -> new TemplateDataOfModelField(item)).collect(Collectors.toList());
    }

    public List<TemplateDataOfModelQuery> getQueries() {
        return new ArrayList<>();
    }

}