package cn.asany.shanhai.core.utils;

import cn.asany.shanhai.core.bean.*;
import cn.asany.shanhai.core.bean.enums.ModelConnectType;
import cn.asany.shanhai.core.bean.enums.ModelType;
import cn.asany.shanhai.core.dao.ModelFieldDao;
import cn.asany.shanhai.core.service.ModelFeatureService;
import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.core.support.model.FieldType;
import cn.asany.shanhai.core.support.model.IModelFeature;
import cn.asany.shanhai.core.support.model.ModelFeatureRegistry;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.util.PinyinUtils;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.ognl.OgnlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class ModelUtils {

    @Autowired
    private ModelService modelService;
    @Autowired
    private ModelFeatureService modelFeatureService;
    @Autowired
    private ModelFeatureRegistry modelFeatureRegistry;
    @Autowired
    private ModelFieldDao modelFieldDao;

    private OgnlUtil ognlUtil = OgnlUtil.getInstance();

    @SneakyThrows
    public void initialize(Model model) {
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
    public ModelField install(Model model, ModelField field) {
        field.setModel(model);
        field.setPrimaryKey(ObjectUtil.defaultValue(field.getPrimaryKey(), Boolean.FALSE));
        field.setCode(ObjectUtil.defaultValue(field.getCode(), StringUtil.lowerCaseFirst(StringUtil.camelCase(PinyinUtils.getAll(field.getName())))));
        if (model.getType() != ModelType.OBJECT) {
            return field;
        }
        ModelFieldMetadata metadata = field.getMetadata();
        if (metadata == null) {
            field.setMetadata(metadata = ModelFieldMetadata.builder().field(field).build());
        }
        metadata.setUnique(field.getUnique());
        if (StringUtil.isBlank(metadata.getDatabaseColumnName())) {
            metadata.setDatabaseColumnName(StringUtil.snakeCase(field.getCode()).toUpperCase());
        }
        metadata.setField(field);
        return field;
    }

    public void reinstall(Model model, ModelField field) {
        ModelField dest = ObjectUtil.find(model.getFields(), "id", field.getId());
        merge(dest, field);
    }

    public void uninstall(Model model, ModelField field) {
        ModelField removeObject = ObjectUtil.remove(model.getFields(), "id", field.getId());
        if (removeObject == null) {
            removeObject = ObjectUtil.remove(model.getFields(), "code", field.getCode());
        }
        modelFieldDao.delete(removeObject);
    }

    public Optional<ModelField> getId(Model model) {
        return model.getFields().stream().filter(item -> ObjectUtil.defaultValue(item.getPrimaryKey(), Boolean.FALSE)).findAny();
    }

    public Model getModelByCode(String code) {
        Optional<Model> optional = modelService.findByCode(code);
        if (!optional.isPresent()) {
            throw new ValidationException("MODEL CODE:" + code + "不存在");
        }
        return Model.builder().id(optional.get().getId()).build();
    }

    public ModelField generatePrimaryKeyField() {
        return ModelField.builder().code(FieldType.ID.getCode().toLowerCase()).name(FieldType.ID.getCode()).type(FieldType.ID).system(true).primaryKey(true).build();
    }

    public List<ModelField> getFields(Model model) {
        return model.getFields().stream().filter(item -> !item.getPrimaryKey()).collect(Collectors.toList());
    }

    public void install(Model model, ModelFeature modelFeature) {
        Optional<ModelFeature> optionalModelFeature = modelFeatureService.get(modelFeature.getId());
        if (!optionalModelFeature.isPresent()) {
            throw new ValidationException("0000000", String.format("模型特征[%s]不存在", modelFeature.getId()));
        }
        IModelFeature feature = modelFeatureRegistry.get(modelFeature.getId());
        List<ModelField> fields = model.getFields();
        // 设置 Field
        for (ModelField field : feature.fields()) {
            fields.add(this.install(model, field));
        }

        for (Model type : feature.getInputTypes(model)) {
            Optional<Model> optional = modelService.findByCode(type.getCode());
            if (optional.isPresent()) {
                model.connect(optional.get(), ModelConnectType.INPUT);
            } else {
                model.connect(modelService.save(type), ModelConnectType.INPUT);
            }
        }

        // 设置 Endpoint
        model.getEndpoints().addAll(feature.getEndpoints(model));
    }

    public void reinstall(Model model, ModelFeature modelFeature) {
        IModelFeature feature = modelFeatureRegistry.get(modelFeature.getId());
        List<ModelField> fields = model.getFields();
        // 设置 Field
        for (ModelField field : feature.fields()) {
            if (ObjectUtil.exists(fields, "code", field.getCode())) {
                continue;
            }
            fields.add(this.install(model, field));
        }

        for (Model type : feature.getInputTypes(model)) {
            Optional<Model> optional = modelService.findByCode(type.getCode());
            if (optional.isPresent()) { // 如果之前存在对象，查询填充 ID 字段
                Model oldType = optional.get();
                type.setId(oldType.getId());
                for (ModelField field : type.getFields()) {
                    ModelField oldField = ObjectUtil.find(oldType.getFields(), "code", field.getCode());
                    if (oldField == null) {
                        continue;
                    }
                    field.setId(oldField.getId());
                }
                model.connect(modelService.update(type), ModelConnectType.INPUT);
            } else {
                model.connect(modelService.save(type), ModelConnectType.INPUT);
            }
        }

        // 设置 Endpoint
        // model.getEndpoints().addAll(feature.getEndpoints(model));
    }

    public void uninstall(Model model, ModelFeature modelFeature) {
        IModelFeature feature = modelFeatureRegistry.get(modelFeature.getId());
        // 卸载字段
        for (ModelField field : feature.fields()) {
            this.uninstall(model, field);
        }
        ObjectUtil.remove(model.getFeatures(), "id", modelFeature.getId());
    }

    public void install(Model model, ModelEndpoint endpoint) {
        for (ModelEndpointArgument argument : ObjectUtil.defaultValue(endpoint.getArguments(), Collections.<ModelEndpointArgument>emptyList())) {
            argument.setEndpoint(endpoint);
            argument.setType(this.getModelByCode(argument.getType().getCode()));
        }
        endpoint.setModel(model);
    }

    public void mergeFields(Model model, List<ModelField> nextFields) {
        List<ModelField> prevFields = model.getFields();
        DiffObject diffObject = diff(prevFields.stream().filter(item -> !item.getSystem()).collect(Collectors.toList()), nextFields, (prev, next) -> prev.equals(next) ? 0 : -1);

        List<ModelField> newFields = diffObject.getAppendItems();
        List<ModelField> oldFields = diffObject.getModifiedItems();
        List<ModelField> removeFields = diffObject.getDeletedItems();

        for (ModelField field : removeFields) {
            this.uninstall(model, field);
        }
        for (ModelField field : oldFields) {
            this.reinstall(model, field);
        }
        prevFields.addAll(newFields);
        for (ModelField field : newFields) {
            this.install(model, field);
        }
    }

    public void mergeFeatures(Model model, List<ModelFeature> nextFeatures) {
        List<ModelFeature> prevFeatures = model.getFeatures();
        DiffObject diffObject = diff(prevFeatures, nextFeatures, (prev, next) -> prev.equals(next) ? 0 : -1);
        List<ModelFeature> newFeatures = diffObject.getAppendItems();
        List<ModelFeature> oldFeatures = diffObject.getModifiedItems();
        List<ModelFeature> removeFeatures = diffObject.getDeletedItems();

        for (ModelFeature feature : removeFeatures) {
            this.uninstall(model, feature);
        }
        for (ModelFeature feature : oldFeatures) {
            this.reinstall(model, feature);
        }
        prevFeatures.addAll(newFeatures);
        for (ModelFeature feature : newFeatures) {
            this.install(model, feature);
        }
    }

    public <T> DiffObject diff(List<T> prev, List<T> next, Comparator<T> comparator) {
        DiffObject diffObject = new DiffObject();
        List<T> olds = new ArrayList<>(prev);
        for (T obj : next) {
            if (exists(olds, item -> comparator.compare(item, obj) != -1)) {
                remove(olds, item -> comparator.compare(item, obj) != -1);
                diffObject.modifiedItems.add(obj);
            } else {
                diffObject.appendItems.add(obj);
            }
        }
        diffObject.setDeletedItems(olds);
        return diffObject;
    }

    public static <T> T remove(List<T> orig, Predicate<T> selector) {
        if (orig == null) {
            return null;
        }
        int i = indexOf(orig, selector);
        return i == -1 ? null : orig.remove(i);
    }

    public static <T> int indexOf(List<T> objs, Predicate<T> selector) {
        for (int i = 0; i < objs.size(); i++) {
            T obj = objs.get(i);
            if (obj == null) {
                continue;
            }
            if (selector.test(obj)) {
                return i;
            }
        }
        return -1;
    }

    public static <T> Boolean exists(List<T> list, Predicate<T> selector) {
        for (T t : list) {
            if (selector.test(t)) {
                return true;
            }
        }
        return false;
    }

    private <T> void merge(T dest, T... sources) {
        Class entityClass = ClassUtil.getRealClass(dest);
        for (T source : sources) {
            mergeColumn(entityClass, dest, source);
        }
    }

    private <T> void mergeColumn(Class entityClass, T dest, T source) {
        for (Field field : ClassUtil.getDeclaredFields(entityClass, Column.class)) {
            Object value = ognlUtil.getValue(field.getName(), source);
            if (value != null) {
                ClassUtil.setValue(dest, field.getName(), value);
            }
        }
    }

    @Data
    @NoArgsConstructor
    static class DiffObject<T> {
        private List<T> appendItems = new ArrayList<>();
        private List<T> modifiedItems = new ArrayList<>();
        private List<T> deletedItems = new ArrayList<>();
    }

}
