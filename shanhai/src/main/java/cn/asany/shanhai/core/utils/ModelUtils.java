package cn.asany.shanhai.core.utils;

import cn.asany.shanhai.core.dao.ModelDao;
import cn.asany.shanhai.core.dao.ModelDelegateDao;
import cn.asany.shanhai.core.dao.ModelEndpointDao;
import cn.asany.shanhai.core.dao.ModelFieldDao;
import cn.asany.shanhai.core.domain.*;
import cn.asany.shanhai.core.domain.enums.ModelConnectType;
import cn.asany.shanhai.core.domain.enums.ModelDelegateType;
import cn.asany.shanhai.core.domain.enums.ModelType;
import cn.asany.shanhai.core.service.ModelFeatureService;
import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.core.support.graphql.resolvers.BaseDataFetcher;
import cn.asany.shanhai.core.support.graphql.resolvers.DelegateDataFetcher;
import cn.asany.shanhai.core.support.model.FieldType;
import cn.asany.shanhai.core.support.model.FieldTypeRegistry;
import cn.asany.shanhai.core.support.model.IModelFeature;
import cn.asany.shanhai.core.support.model.ModelFeatureRegistry;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.spring.SpringBeanUtils;
import org.jfantasy.framework.util.PinyinUtils;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.ognl.OgnlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ModelUtils {

  @Autowired private ModelService modelService;
  @Autowired private ModelFeatureService modelFeatureService;
  @Autowired private ModelFeatureRegistry modelFeatureRegistry;
  @Autowired private FieldTypeRegistry fieldTypeRegistry;
  @Autowired private ModelFieldDao modelFieldDao;
  @Autowired private ModelDao modelDao;
  @Autowired private ModelDelegateDao modelDelegateDao;
  @Autowired private ModelEndpointDao modelEndpointDao;

  private static final ThreadLocal<ModelUtilCache> HOLDER = new ThreadLocal<>();
  private static final ThreadLocal<ModelLazySaveContext> LAZY = new ThreadLocal<>();

  private OgnlUtil ognlUtil = OgnlUtil.getInstance();

  public static ModelUtils getInstance() {
    return SpringBeanUtils.getBeanByType(ModelUtils.class);
  }

  public static final Set<Model> DEFAULT_TYPES = new HashSet<>();

  public static final Set<Model> DEFAULT_SCALARS = new HashSet<>();

  static {
    DEFAULT_SCALARS.add(FieldType.ID);
    DEFAULT_SCALARS.add(FieldType.Int);
    DEFAULT_SCALARS.add(FieldType.Float);
    DEFAULT_SCALARS.add(FieldType.String);
    DEFAULT_SCALARS.add(FieldType.Boolean);
    DEFAULT_SCALARS.add(FieldType.Date);
    DEFAULT_TYPES.addAll(DEFAULT_SCALARS);
    DEFAULT_TYPES.add(FieldType.Query);
    DEFAULT_TYPES.add(FieldType.Mutation);
  }

  public void enableLazySave() {
    LAZY.set(new ModelLazySaveContext());
  }

  public void disableLazySave() {
    LAZY.remove();
  }

  public boolean isLazySave() {
    ModelLazySaveContext context = LAZY.get();
    return context != null;
  }

  public void lazySave(ModelField modelField) {
    ModelLazySaveContext context = LAZY.get();
    context.addLazySave(LazySaveOperations.SAVE, modelField);
  }

  public void lazySave(Model model) {
    ModelLazySaveContext context = LAZY.get();
    Optional<Model> optional = this.getModelByCode(model.getCode());
    if (!optional.isPresent()) {
      context.addLazySave(LazySaveOperations.SAVE, model);
      this.cache(model);
      return;
    }
    Model source = optional.get();
    source.getFields().addAll(model.getFields());
    context.addLazySave(LazySaveOperations.UPDATE, source);
  }

  @SneakyThrows
  public void initialize(Model model) {
    model.setType(ObjectUtil.defaultValue(model.getType(), ModelType.ENTITY));
    model.setCode(
        ObjectUtil.defaultValue(
            model.getCode(),
            () ->
                StringUtil.upperCaseFirst(
                    StringUtil.camelCase(PinyinUtils.getAll(model.getName())))));
    model.setFields((ObjectUtil.defaultValue(model.getFields(), Collections.emptySet())));
    model.setFeatures(ObjectUtil.defaultValue(model.getFeatures(), Collections.emptySet()));
    model.setEndpoints(ObjectUtil.defaultValue(model.getEndpoints(), Collections.emptySet()));
    model.setRelations(ObjectUtil.defaultValue(model.getRelations(), Collections.emptySet()));
    model.setImplementz(ObjectUtil.defaultValue(model.getImplementz(), Collections.emptySet()));
    model.setMemberTypes(ObjectUtil.defaultValue(model.getMemberTypes(), Collections.emptySet()));

    if (StringUtil.isNotBlank(model.getName()) && StringUtil.length(model.getName()) > 100) {
      if (StringUtil.isBlank(model.getDescription())) {
        model.setDescription(model.getName());
      }
      model.setName(StringUtil.ellipsis(model.getName(), 100, "..."));
    }

    model.setImplementz(
        model.getImplementz().stream()
            .map(
                item -> {
                  Optional<Model> optional = this.getModelByCode(item.getCode());
                  if (!optional.isPresent()) {
                    throw new TypeNotFoundException(item.getCode());
                  }
                  return optional.get();
                })
            .collect(Collectors.toSet()));

    model.setMemberTypes(
        model.getMemberTypes().stream()
            .map(
                item -> {
                  Optional<Model> optional = this.getModelByCode(item.getCode());
                  if (!optional.isPresent()) {
                    throw new TypeNotFoundException(item.getCode());
                  }
                  return optional.get();
                })
            .collect(Collectors.toSet()));

    if (model.getType() != ModelType.ENTITY) {
      return;
    }

    ModelMetadata metadata = model.getMetadata();
    if (metadata == null) {
      model.setMetadata(metadata = ModelMetadata.builder().model(model).build());
    } else {
      metadata.setModel(model);
    }
    if (StringUtil.isBlank(metadata.getDatabaseTableName())) {
      metadata.setDatabaseTableName("DM_" + StringUtil.snakeCase(model.getCode()).toUpperCase());
    }
  }

  public ModelField install(Model model, ModelField field) throws TypeNotFoundException {
    if (!ObjectUtil.exists(model.getFields(), "code", field.getCode())) {
      field.setModel(model);
    }
    field.setPrimaryKey(ObjectUtil.defaultValue(field.getPrimaryKey(), Boolean.FALSE));
    field.setCode(
        ObjectUtil.defaultValue(
            field.getCode(),
            () ->
                StringUtil.lowerCaseFirst(
                    StringUtil.camelCase(PinyinUtils.getAll(field.getName())))));

    if (StringUtil.isNotBlank(field.getName()) && StringUtil.length(field.getName()) > 100) {
      if (StringUtil.isBlank(field.getDescription())) {
        field.setDescription(field.getName());
      }
      field.setName(StringUtil.ellipsis(field.getName(), 100, "..."));
    }

    if (model.getType() == ModelType.ENUM) {
      return field;
    }

    if (field.getType().getId() == null) {
      Optional<Model> type = getModelByCode(field.getType().getCode());
      if (!type.isPresent()) {
        throw new TypeNotFoundException(field.getType().getCode());
      }
      field.setType(type.get());
    }

    for (ModelFieldArgument argument : field.getArguments()) {
      Optional<Model> type = getModelByCode(argument.getType().getCode());
      if (!type.isPresent()) {
        throw new TypeNotFoundException(field.getType().getCode());
      }
      argument.setType(type.get());
    }

    if (model.getType() != ModelType.ENTITY) {
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

    if (metadata.getFilters() == null && field.getType().getType() == ModelType.SCALAR) {
      metadata.setFilters(fieldTypeRegistry.getType(field.getType().getCode()).filters());
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
    return model.getFields().stream()
        .filter(item -> ObjectUtil.defaultValue(item.getPrimaryKey(), Boolean.FALSE))
        .findAny();
  }

  public Optional<Model> getModelByCode(String code) {
    ModelUtilCache cache = this.cache();
    if (this.isLazySave()) {
      return cache.getModelByCode(code, Optional::empty);
    }
    return cache.getModelByCode(
        code, () -> this.modelDao.findOne(Example.of(Model.builder().code(code).build())));
  }

  public Optional<Model> getModelById(Long id) {
    ModelUtilCache cache = this.cache();
    if (this.isLazySave()) {
      return cache.getModelById(id, () -> Optional.empty());
    }
    return cache.getModelById(id, () -> this.modelDao.findById(id));
  }

  //    public Model getModelByCode(Model model, String code) {
  //        if (model.getCode().equals(code)) {
  //            return model;
  //        }
  //        Optional<Model> modelType = model.getRelations().stream().map(item ->
  // item.getInverse()).filter(item -> item.getCode().equals(code)).findAny();
  //        return modelType.orElseGet(() -> this.getModelByCode(code).get());
  //    }

  public ModelField generatePrimaryKeyField() {
    return ModelField.builder()
        .code(FieldType.ID.getCode().toLowerCase())
        .name(FieldType.ID.getCode())
        .type(FieldType.ID)
        .system(true)
        .primaryKey(true)
        .build();
  }

  public List<ModelField> getFields(Model model) {
    return model.getFields().stream()
        .filter(item -> !item.getPrimaryKey())
        .collect(Collectors.toList());
  }

  @SneakyThrows
  public void install(Model model, ModelFeature modelFeature) {
    Optional<ModelFeature> optionalModelFeature = modelFeatureService.get(modelFeature.getId());
    if (!optionalModelFeature.isPresent()) {
      throw new ValidationException("0000000", String.format("模型特征[%s]不存在", modelFeature.getId()));
    }
    IModelFeature feature = modelFeatureRegistry.get(modelFeature.getId());
    Set<ModelField> fields = model.getFields();
    // 设置 Field
    for (ModelField field : feature.fields()) {
      fields.add(this.install(model, field));
    }

    for (Model type : feature.getTypes(model)) {
      Optional<Model> optional = this.getModelByCode(type.getCode());
      if (optional.isPresent()) {
        model.connect(optional.get(), getModelConnectType(type.getType()));
      } else {
        model.connect(modelService.save(type, this), getModelConnectType(type.getType()));
      }
    }

    for (ModelEndpoint endpoint : feature.getEndpoints(model)) {
      this.install(model, endpoint);
    }
  }

  private ModelConnectType getModelConnectType(ModelType type) {
    if (ModelType.INPUT_OBJECT == type) {
      return ModelConnectType.INPUT;
    } else if (ModelType.OBJECT == type || ModelType.ENUM == type) {
      return ModelConnectType.TYPE;
    }
    return null;
  }

  @SneakyThrows
  public void reinstall(Model model, ModelFeature modelFeature) {
    IModelFeature feature = modelFeatureRegistry.get(modelFeature.getId());
    Set<ModelField> fields = model.getFields();
    // 设置 Field
    for (ModelField field : feature.fields()) {
      if (ObjectUtil.exists(fields, "code", field.getCode())) {
        continue;
      }
      fields.add(this.install(model, field));
    }

    for (Model type : feature.getTypes(model)) {
      Optional<Model> optional = this.getModelByCode(type.getCode());
      // 如果之前存在对象，查询填充 ID 字段
      if (optional.isPresent()) {
        Model oldType = optional.get();
        type.setId(oldType.getId());
        for (ModelField field : type.getFields()) {
          ModelField oldField = ObjectUtil.find(oldType.getFields(), "code", field.getCode());
          if (oldField == null) {
            continue;
          }
          field.setId(oldField.getId());
        }
        model.connect(modelService.update(type), getModelConnectType(type.getType()));
      } else {
        model.connect(modelService.save(type, this), getModelConnectType(type.getType()));
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
    for (ModelEndpointArgument argument :
        ObjectUtil.defaultValue(
            endpoint.getArguments(), Collections.<ModelEndpointArgument>emptyList())) {
      argument.setEndpoint(endpoint);
      if (argument.getType().getId() != null) {
        continue;
      }
      argument.setType(this.getModelByCode(argument.getType().getCode()).get());
    }
    ModelEndpointReturnType returnType = endpoint.getReturnType();
    returnType.setEndpoint(endpoint);
    if (returnType.getType().getId() == null) {
      returnType.setType(this.getModelByCode(returnType.getType().getCode()).get());
    }
    endpoint.setModel(model);
    modelEndpointDao.save(endpoint);
    if (!ObjectUtil.exists(model.getEndpoints(), "code", endpoint.getCode())) {
      model.getEndpoints().add(endpoint);
    }
  }

  @SneakyThrows
  public void mergeFields(Model model, Set<ModelField> nextFields) {
    Set<ModelField> prevFields = model.getFields();
    DiffObject diffObject =
        diff(
            prevFields.stream().filter(item -> !item.getSystem()).collect(Collectors.toList()),
            nextFields,
            (prev, next) -> prev.equals(next) ? 0 : -1);

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

  public void mergeFeatures(Model model, Set<ModelFeature> nextFeatures) {
    Set<ModelFeature> prevFeatures = model.getFeatures();
    DiffObject diffObject =
        diff(prevFeatures, nextFeatures, (prev, next) -> prev.equals(next) ? 0 : -1);
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

  public <T> DiffObject diff(Collection<T> prev, Collection<T> next, Comparator<T> comparator) {
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

  public ModelDelegate getDelegate(Class<? extends DelegateDataFetcher> resolverClass) {
    if (BaseDataFetcher.class.isAssignableFrom(resolverClass)) {
      Optional<ModelDelegate> optional =
          modelDelegateDao.findOne(
              Example.of(
                  ModelDelegate.builder()
                      .type(ModelDelegateType.Base)
                      .delegateClassName(resolverClass.getName())
                      .build()));
      return optional.orElseGet(
          () ->
              ModelDelegate.builder()
                  .type(ModelDelegateType.Base)
                  .delegateClassName(resolverClass.getName())
                  .build());
    }
    return null;
  }

  public void clear() {
    this.HOLDER.remove();
  }

  public ModelUtilCache newCache(List<Model> types) {
    ModelUtilCache cache = HOLDER.get();
    if (cache == null) {
      HOLDER.set(new ModelUtilCache(types));
      return HOLDER.get();
    }
    return HOLDER.get();
  }

  public ModelUtilCache newCache(List<Model> types, List<Model> inputTypes, List<Model> scalars) {
    List<Model> newTypes = new ArrayList<>();
    newTypes.addAll(types);
    newTypes.addAll(inputTypes);
    newTypes.addAll(scalars);
    return newCache(newTypes);
  }

  public ModelUtilCache cache() {
    ModelUtilCache cache = HOLDER.get();
    if (cache == null) {
      HOLDER.set(new ModelUtilCache());
      return HOLDER.get();
    }
    return HOLDER.get();
  }

  public void cache(Model result) {
    ModelUtilCache cache = HOLDER.get();
    if (cache == null) {
      return;
    }
    cache.putModel(result);
  }

  public Model saveWithCache(Model entity) {
    if (!this.isLazySave()) {
      this.modelDao.save(entity);
      // 添加到缓存
      this.cache(entity);
    } else {
      this.lazySave(entity);
    }
    return entity;
  }

  public ModelLazySaveContext getModelLazySaveContext() {
    return LAZY.get();
  }

  @Data
  @NoArgsConstructor
  static class DiffObject<T> {
    private List<T> appendItems = new ArrayList<>();
    private List<T> modifiedItems = new ArrayList<>();
    private List<T> deletedItems = new ArrayList<>();
  }

  public static class ModelUtilCache {
    private List<Model> models = new ArrayList<>();
    private final List<ModelField> fields = new ArrayList<>();
    private Map<Long, Model> modelsById = new HashMap<>();
    private Map<String, Model> modelsByCode = new HashMap<>();

    public ModelUtilCache() {}

    public ModelUtilCache(List<Model> types) {
      for (Model model : types) {
        this.putModel(model);
      }
    }

    public boolean containsModel(Long id) {
      return modelsById.containsKey(id);
    }

    public Optional<Model> getModelById(Long id, Supplier<Optional<Model>> override) {
      if (modelsById.containsKey(id)) {
        return Optional.of(modelsById.get(id));
      }
      Optional<Model> optional = override.get();
      if (optional.isPresent()) {
        this.putModel(optional.get());
        return optional;
      }
      return Optional.empty();
    }

    public Optional<Model> getModelByCode(String code, Supplier<Optional<Model>> override) {
      if (modelsByCode.containsKey(code)) {
        return Optional.of(modelsByCode.get(code));
      }
      Optional<Model> optional = override.get();
      if (optional.isPresent()) {
        this.putModel(optional.get());
        return optional;
      }
      return Optional.empty();
    }

    private void putModel(Model model) {
      this.modelsById.put(model.getId(), model);
      this.modelsByCode.put(model.getCode(), model);
    }
  }

  public static class ModelLazySaveContext {
    public final List<Model> saves = new ArrayList<>();
    public final List<Model> updates = new ArrayList<>();
    public final List<ModelField> savesByField = new ArrayList<>();
    public final List<ModelField> updatesByField = new ArrayList<>();

    public void addLazySave(LazySaveOperations operations, Model model) {
      if (operations == LazySaveOperations.SAVE) {
        saves.add(model);
      } else {
        updates.add(model);
      }
    }

    public void addLazySave(LazySaveOperations operations, ModelField field) {
      if (operations == LazySaveOperations.SAVE) {
        savesByField.add(field);
      } else {
        updatesByField.add(field);
      }
    }

    public void addLazySave(LazySaveOperations operations, Set<ModelField> fields) {
      if (operations == LazySaveOperations.SAVE) {
        this.savesByField.addAll(fields);
      } else {
        this.updatesByField.addAll(fields);
      }
    }
  }

  public enum LazySaveOperations {
    SAVE,
    UPDATE
  }
}
