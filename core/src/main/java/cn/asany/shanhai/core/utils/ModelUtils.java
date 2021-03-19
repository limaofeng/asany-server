package cn.asany.shanhai.core.utils;

import cn.asany.shanhai.core.bean.*;
import cn.asany.shanhai.core.bean.enums.ModelConnectType;
import cn.asany.shanhai.core.bean.enums.ModelType;
import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.core.support.model.FieldType;
import cn.asany.shanhai.core.support.model.IModelFeature;
import lombok.SneakyThrows;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.util.PinyinUtils;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ModelUtils {

    @Autowired
    private ModelService modelService;

    @SneakyThrows
    public void inject(Model model) {
        model.setType(ObjectUtil.defaultValue(model.getType(), ModelType.OBJECT));
        model.setCode(ObjectUtil.defaultValue(model.getCode(), () -> StringUtil.upperCaseFirst(StringUtil.camelCase(PinyinUtils.getAll(model.getName())))));
        model.setFields(new ArrayList<>((ObjectUtil.defaultValue(model.getFields(), Collections.emptyList()))));
        model.setFeatures(new ArrayList<>(ObjectUtil.defaultValue(model.getFeatures(), Collections.emptyList())));
        model.setEndpoints(new ArrayList<>(ObjectUtil.defaultValue(model.getEndpoints(), Collections.emptyList())));
        model.setRelations(new ArrayList<>(ObjectUtil.defaultValue(model.getRelations(), Collections.emptyList())));

        if (model.getType() == ModelType.SCALAR || model.getType() == ModelType.INPUT) {
            return;
        }

        ModelMetadata metadata = model.getMetadata();
        if (metadata == null) {
            model.setMetadata(metadata = ModelMetadata.builder().model(model).build());
        } else {
            metadata.setId(model.getId());
        }
        if (StringUtil.isBlank(metadata.getDatabaseTableName())) {
            metadata.setDatabaseTableName("DM_" + StringUtil.snakeCase(model.getCode()).toUpperCase());
        }
    }

    @SneakyThrows
    public void inject(Model model, ModelField field) {
        field.setModel(model);
        field.setIsPrimaryKey(ObjectUtil.defaultValue(field.getIsPrimaryKey(), Boolean.FALSE));
        field.setCode(ObjectUtil.defaultValue(field.getCode(), StringUtil.lowerCaseFirst(StringUtil.camelCase(PinyinUtils.getAll(field.getName())))));
        if (model.getType() != ModelType.OBJECT) {
            return;
        }
        ModelFieldMetadata metadata = field.getMetadata();
        if (metadata == null) {
            field.setMetadata(metadata = ModelFieldMetadata.builder().field(field).build());
        }
        metadata.setUnique(field.getIsUnique());
        if (StringUtil.isBlank(metadata.getDatabaseColumnName())) {
            metadata.setDatabaseColumnName(StringUtil.snakeCase(field.getCode()).toUpperCase());
        }
        metadata.setField(field);
    }

    public Optional<ModelField> getId(Model model) {
        return model.getFields().stream().filter(item -> ObjectUtil.defaultValue(item.getIsPrimaryKey(), Boolean.FALSE)).findAny();
    }

    public Model getModelByCode(String code) {
        Optional<Model> optional = modelService.findByCode(code);
        if (!optional.isPresent()) {
            throw new ValidationException("MODEL CODE:" + code + "不存在");
        }
        return Model.builder().id(optional.get().getId()).build();
    }

    public ModelField generatePrimaryKeyField() {
        return ModelField.builder().code(FieldType.ID.getCode().toLowerCase()).name(FieldType.ID.getCode()).type(FieldType.ID).isPrimaryKey(true).build();
    }

    public List<ModelField> getFields(Model model) {
        return model.getFields().stream().filter(item -> !item.getIsPrimaryKey()).collect(Collectors.toList());
    }

    public void inject(Model model, IModelFeature feature) {
        List<ModelField> fields = model.getFields();
        // 设置 Field
        for (ModelField field : feature.fields()) {
            field.setModel(model);
            fields.add(field);
        }

        for (Model type : feature.getInputTypes(model)) {
            if (!modelService.exists(type.getCode())) {
                modelService.save(type);
            }
            model.connect(type, ModelConnectType.INPUT);
        }

        List<ModelEndpoint> endpoints = model.getEndpoints();
        // 设置 Endpoint
        for (ModelEndpoint endpoint : feature.getEndpoints(model.getMetadata())) {
            endpoint.setModel(model);
            endpoints.add(endpoint);
        }
    }
}
