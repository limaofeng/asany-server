package cn.asany.shanhai.core.utils;

import cn.asany.shanhai.core.bean.*;
import cn.asany.shanhai.core.support.model.FieldType;
import cn.asany.shanhai.core.support.model.IModelFeature;
import lombok.SneakyThrows;
import org.jfantasy.framework.util.PinyinUtils;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ModelUtils {

    private static final String CONSTANT_FIELD_NAME_ID = "id";

    @SneakyThrows
    public static void inject(Model model) {
        ModelMetadata metadata = model.getMetadata();
        if (metadata == null) {
            model.setMetadata(metadata = ModelMetadata.builder().model(model).build());
        }
        if (StringUtil.isBlank(metadata.getName())) {
            metadata.setName(StringUtil.upperCaseFirst(StringUtil.camelCase(PinyinUtils.getAll(model.getName()))));
        }
        if (StringUtil.isBlank(metadata.getDatabaseTableName())) {
            metadata.setDatabaseTableName("DM_" + StringUtil.snakeCase(metadata.getName()).toUpperCase());
        }
    }

    @SneakyThrows
    public static void inject(ModelField field) {
        field.setIsPrimaryKey(ObjectUtil.defaultValue(field.getIsPrimaryKey(), Boolean.FALSE));
        ModelFieldMetadata metadata = field.getMetadata();
        if (metadata == null) {
            field.setMetadata(metadata = ModelFieldMetadata.builder().field(field).build());
        }
        metadata.setUnique(field.getIsUnique());
        if (StringUtil.isBlank(metadata.getName())) {
            metadata.setName(StringUtil.lowerCaseFirst(StringUtil.camelCase(PinyinUtils.getAll(field.getName()))));
        }
        if (StringUtil.isBlank(metadata.getDatabaseColumnName())) {
            metadata.setDatabaseColumnName(StringUtil.snakeCase(metadata.getName()).toUpperCase());
        }
        metadata.setField(field);
    }

    public static Optional<ModelField> getId(Model model) {
        return model.getFields().stream().filter(item -> ObjectUtil.defaultValue(item.getIsPrimaryKey(), Boolean.FALSE)).findAny();
    }

    public static ModelField generatePrimaryKeyField() {
        return ModelField.builder().name(CONSTANT_FIELD_NAME_ID).type(FieldType.ID).isPrimaryKey(true).metadata(FieldType.ID.toLowerCase(), FieldType.ID.toUpperCase()).build();
    }

    public static List<ModelField> getFields(Model model) {
        return model.getFields().stream().filter(item -> !item.getIsPrimaryKey()).collect(Collectors.toList());
    }

    public static void inject(Model model, IModelFeature feature) {
        List<ModelField> fields = model.getFields();
        // 设置 Field
        for (ModelField field : feature.fields()) {
            field.setModel(model);
            fields.add(field);
        }

        List<ModelEndpoint> endpoints = model.getEndpoints();
        // 设置 Endpoint
        for (ModelEndpoint endpoint : feature.getEndpoints(model.getMetadata())) {
            endpoint.setModel(model);
            endpoints.add(endpoint);
        }
    }
}
