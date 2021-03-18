package cn.asany.shanhai.core.utils;

import cn.asany.shanhai.core.bean.*;
import cn.asany.shanhai.core.bean.enums.ModelType;
import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.core.support.model.FieldType;
import cn.asany.shanhai.core.support.model.IModelFeature;
import com.github.stuxuhai.jpinyin.PinyinException;
import lombok.SneakyThrows;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.util.PinyinUtils;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

public class ModelUtils {

    private static ModelService _modelService;

    private static ModelService modelService() {
        if (_modelService == null) {
            _modelService = SpringContextUtil.getBeanByType(ModelService.class);
        }
        return _modelService;
    }

    @SneakyThrows
    public static void inject(Model model) {
        model.setType(ObjectUtil.defaultValue(model.getType(), ModelType.OBJECT));
        model.setCode(ObjectUtil.defaultValue(model.getCode(), () -> StringUtil.upperCaseFirst(StringUtil.camelCase(PinyinUtils.getAll(model.getName())))));
        model.setFields(new ArrayList<>((ObjectUtil.defaultValue(model.getFields(), Collections.emptyList()))));
        model.setFeatures(new ArrayList<>(ObjectUtil.defaultValue(model.getFeatures(), Collections.emptyList())));
        model.setEndpoints(new ArrayList<>(ObjectUtil.defaultValue(model.getEndpoints(), Collections.emptyList())));

        if (model.getType() == ModelType.SCALAR) {
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
    public static void inject(ModelField field) {
        field.setIsPrimaryKey(ObjectUtil.defaultValue(field.getIsPrimaryKey(), Boolean.FALSE));
        field.setCode(ObjectUtil.defaultValue(field.getCode(), StringUtil.lowerCaseFirst(StringUtil.camelCase(PinyinUtils.getAll(field.getName())))));
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

    public static Optional<ModelField> getId(Model model) {
        return model.getFields().stream().filter(item -> ObjectUtil.defaultValue(item.getIsPrimaryKey(), Boolean.FALSE)).findAny();
    }

    public static Model getModelByCode(String code) {
        Optional<Model> optional = modelService().findByCode(code);
        if (!optional.isPresent()) {
            throw new ValidationException("MODEL CODE:" + code + "不存在");
        }
        return Model.builder().id(optional.get().getId()).build();
    }

    public static ModelField generatePrimaryKeyField() {
        return ModelField.builder().code(FieldType.ID.getCode().toLowerCase()).name(FieldType.ID.getCode()).type(FieldType.ID).isPrimaryKey(true).build();
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
