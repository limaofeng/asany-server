package cn.asany.shanhai.core.utils;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.ModelField;
import lombok.AllArgsConstructor;
import org.jfantasy.framework.spring.SpringContextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class TemplateDataOfModel {
    private Model model;

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
        ModelUtils modelUtils = SpringContextUtil.getBeanByType(ModelUtils.class);
        Optional<ModelField> idFieldOptional = modelUtils.getId(model);
        return idFieldOptional.map(TemplateDataOfModelField::new).orElse(null);
    }

    public List<TemplateDataOfModelField> getFields() {
        ModelUtils modelUtils = SpringContextUtil.getBeanByType(ModelUtils.class);
        return modelUtils.getFields(model).stream().map(item -> new TemplateDataOfModelField(item)).collect(Collectors.toList());
    }

    public List<TemplateDataOfModelQuery> getQueries() {
        return new ArrayList<>();
    }

}