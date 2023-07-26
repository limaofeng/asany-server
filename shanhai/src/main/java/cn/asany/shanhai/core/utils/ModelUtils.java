package cn.asany.shanhai.core.utils;

import cn.asany.shanhai.core.dao.ModelDao;
import cn.asany.shanhai.core.dao.ModelDelegateDao;
import cn.asany.shanhai.core.dao.ModelEndpointDao;
import cn.asany.shanhai.core.dao.ModelFieldDao;
import cn.asany.shanhai.core.domain.*;
import cn.asany.shanhai.core.domain.enums.ModelDelegateType;
import cn.asany.shanhai.core.domain.enums.ModelType;
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
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.spring.SpringBeanUtils;
import org.jfantasy.framework.util.PinyinUtils;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.common.toys.CompareResults;
import org.jfantasy.framework.util.ognl.OgnlUtil;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class ModelUtils {
  private final ModelFeatureRegistry modelFeatureRegistry;
  private final FieldTypeRegistry fieldTypeRegistry;
  private final ModelFieldDao modelFieldDao;
  private final ModelDelegateDao modelDelegateDao;
  private final ModelEndpointDao modelEndpointDao;
  private final ModelService modelService;
  private static final ThreadLocal<ModelUtilCache> HOLDER = new ThreadLocal<>();
  private static final ThreadLocal<ModelLazySaveContext> LAZY = new ThreadLocal<>();

  private final OgnlUtil ognlUtil = OgnlUtil.getInstance();

  public ModelUtils(
      ModelService modelService,
      ModelFeatureRegistry modelFeatureRegistry,
      FieldTypeRegistry fieldTypeRegistry,
      ModelFieldDao modelFieldDao,
      ModelDao modelDao,
      ModelDelegateDao modelDelegateDao,
      ModelEndpointDao modelEndpointDao) {
    this.modelFeatureRegistry = modelFeatureRegistry;
    this.fieldTypeRegistry = fieldTypeRegistry;
    this.modelFieldDao = modelFieldDao;
    this.modelDelegateDao = modelDelegateDao;
    this.modelEndpointDao = modelEndpointDao;
    this.modelService = modelService;
  }

  public static ModelUtils getInstance() {
    return SpringBeanUtils.getBeanByType(ModelUtils.class);
  }

  public ModelFeatureRegistry getModelFeatureRegistry() {
    return modelFeatureRegistry;
  }

  public static final Set<Model> DEFAULT_TYPES = new HashSet<>();

  public static final Set<Model> DEFAULT_SCALARS = new HashSet<>();

  static {
    Model ID = Model.builder().type(ModelType.SCALAR).id(3L).code("ID").name("ID").build();
    Model Int = Model.builder().type(ModelType.SCALAR).id(4L).code("Int").name("整数型").build();
    Model Float = Model.builder().type(ModelType.SCALAR).id(5L).code("Float").name("浮点型").build();
    Model String = Model.builder().type(ModelType.SCALAR).id(6L).code("String").name("字符串").build();
    Model Boolean =
        Model.builder().type(ModelType.SCALAR).id(7L).code("Boolean").name("布尔型").build();
    Model Date = Model.builder().type(ModelType.SCALAR).id(8L).code("Date").name("日期").build();
    Model File = Model.builder().type(ModelType.SCALAR).id(9L).code("File").name("文件对象").build();
    Model JSON = Model.builder().type(ModelType.SCALAR).id(10L).code("JSON").name("JSON对象").build();

    DEFAULT_SCALARS.add(ID);
    DEFAULT_SCALARS.add(Int);
    DEFAULT_SCALARS.add(Float);
    DEFAULT_SCALARS.add(String);
    DEFAULT_SCALARS.add(Boolean);
    DEFAULT_SCALARS.add(Date);
    DEFAULT_SCALARS.add(File);
    DEFAULT_SCALARS.add(JSON);

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
        StringUtil.upperCaseFirst(
            ObjectUtil.defaultValue(
                model.getCode(), () -> StringUtil.camelCase(PinyinUtils.getAll(model.getName())))));
    model.setFields((ObjectUtil.defaultValue(model.getFields(), new HashSet<>())));
    model.setFeatures(ObjectUtil.defaultValue(model.getFeatures(), new HashSet<>()));
    model.setEndpoints(ObjectUtil.defaultValue(model.getEndpoints(), new HashSet<>()));
    model.setRelations(ObjectUtil.defaultValue(model.getRelations(), new HashSet<>()));
    model.setImplementz(ObjectUtil.defaultValue(model.getImplementz(), new HashSet<>()));
    model.setMemberTypes(ObjectUtil.defaultValue(model.getMemberTypes(), new HashSet<>()));

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
    if (field.getSort() == null) {
      Set<ModelField> nonSystemFields = ObjectUtil.filter(model.getFields(), "system", false);
      field.setSort(ObjectUtil.indexOf(nonSystemFields, "code", field.getCode()));
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

    FieldType<?, ?> fieldType = fieldTypeRegistry.getType(field.getType());

    Optional<Model> realType = getModelByCode(fieldType.getGraphQLType());
    field.setRealType(realType.orElseThrow(() -> new TypeNotFoundException(field.getType())));

    if (field.getArguments() == null) {
      field.setArguments(new HashSet<>());
    }
    for (ModelFieldArgument argument : field.getArguments()) {
      Optional<Model> type = getModelByCode(argument.getType());
      if (!type.isPresent()) {
        throw new TypeNotFoundException(field.getType());
      }
      argument.setRealType(type.get());
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

    if (metadata.getFilters() == null && field.getRealType().getType() == ModelType.SCALAR) {
      metadata.setFilters(fieldTypeRegistry.getType(field.getType()).filters());
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
    return cache.getModelByCode(code, () -> this.modelService.findByCode(code));
  }

  public Optional<Model> getModelById(Long id) {
    ModelUtilCache cache = this.cache();
    if (this.isLazySave()) {
      return cache.getModelById(id, Optional::empty);
    }
    return cache.getModelById(id, () -> this.modelService.findById(id));
  }

  public ModelField generatePrimaryKeyField() {
    return ModelField.builder()
        .code(FieldType.ID.toLowerCase())
        .name(FieldType.ID)
        .type(FieldType.ID)
        .sort(0)
        .required(true)
        .unique(true)
        .system(true)
        .primaryKey(true)
        .build();
  }

  public List<ModelField> getFields(Model model) {
    return model.getFields().stream()
        .filter(item -> !item.getPrimaryKey())
        .collect(Collectors.toList());
  }

  public void preinstall(Model model, ModelFeature modelFeature) {
    IModelFeature feature = modelFeatureRegistry.get(modelFeature.getId());
    Set<ModelField> fields = model.getFields();
    // 设置 Field
    for (ModelField field : feature.fields()) {
      if (ObjectUtil.exists(fields, "code", field.getCode())) {
        continue;
      }
      fields.add(this.install(model, field));
    }
  }

  @SneakyThrows
  public void install(Model model, ModelFeature modelFeature, ModelService modelService) {
    IModelFeature feature = modelFeatureRegistry.get(modelFeature.getId());

    for (ModelRelation relation : feature.getTypes(model)) {
      Model type = relation.getInverse();

      Optional<Model> optional = this.getModelByCode(type.getCode());
      if (optional.isPresent()) {
        type.setId(optional.get().getId());
        model.connect(modelService.save(type, this), relation.getType(), relation.getRelation());
      } else {
        if (this.isLazySave()) {
          this.cache().putModel(type);
        }
        type.setModule(model.getModule());
        model.connect(modelService.save(type, this), relation.getType(), relation.getRelation());
      }
    }
  }

  @SneakyThrows
  public void postinstall(Model model, ModelFeature modelFeature) {
    IModelFeature feature = modelFeatureRegistry.get(modelFeature.getId());

    List<ModelEndpoint> endpoints = feature.getEndpoints(model);
    for (ModelEndpoint endpoint : endpoints) {
      this.install(model, endpoint);
    }
  }

  public void reinstall(Model model, ModelFeature modelFeature, ModelService modelService) {
    IModelFeature feature = modelFeatureRegistry.get(modelFeature.getId());

    for (ModelRelation relation : feature.getTypes(model)) {
      Model type = relation.getInverse();
      boolean isContains = this.modelService.getEntityManager().contains(type);
      Optional<Model> optional =
          isContains ? Optional.of(type) : this.getModelByCode(type.getCode());
      // 如果之前存在对象，查询填充 ID 字段
      if (optional.isPresent()) {
        Model oldType = optional.get();
        if (!isContains) {
          type.setId(oldType.getId());
          for (ModelField field : type.getFields()) {
            ModelField oldField = ObjectUtil.find(oldType.getFields(), "code", field.getCode());
            if (oldField == null || oldField.getId() == null) {
              continue;
            }
            field.setId(oldField.getId());
          }
        }
        model.connect(modelService.update(type), relation.getType(), relation.getRelation());
      } else {
        model.connect(modelService.save(type, this), relation.getType(), relation.getRelation());
      }
    }
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
    Set<ModelEndpointArgument> arguments =
        ObjectUtil.defaultValue(endpoint.getArguments(), Collections.emptySet());
    for (ModelEndpointArgument argument : arguments) {
      if (argument.getEndpoint() == null) {
        argument.setEndpoint(endpoint);
      }
      if (argument.getRealType() != null && argument.getRealType().getId() != null) {
        continue;
      }
      argument.setRealType(
          this.getModelByCode(argument.getType())
              .orElseThrow(() -> new TypeNotFoundException(argument.getType())));
    }
    ModelEndpointReturnType returnType = endpoint.getReturnType();
    returnType.setEndpoint(endpoint);
    if (returnType.getType().getId() == null) {
      returnType.setType(
          this.getModelByCode(returnType.getType().getCode())
              .orElseThrow(() -> new TypeNotFoundException(returnType.getType().getCode())));
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
    CompareResults<ModelField> diffObject =
        ObjectUtil.compare(
            prevFields.stream().filter(item -> !item.getSystem()).collect(Collectors.toList()),
            nextFields,
            (prev, next) -> prev.equals(next) && prev.getId() != null ? 0 : -1);

    List<ModelField> newFields = diffObject.getExceptB();
    List<ModelField> oldFields = diffObject.getIntersect();
    List<ModelField> removeFields = diffObject.getExceptA();

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

  public void mergeFeatures(
      Model model, Set<ModelFeature> nextFeatures, ModelService modelService) {
    Set<ModelFeature> prevFeatures = model.getFeatures();
    CompareResults<ModelFeature> diffObject =
        ObjectUtil.compare(prevFeatures, nextFeatures, (prev, next) -> prev.equals(next) ? 0 : -1);
    List<ModelFeature> newFeatures = diffObject.getExceptB();
    List<ModelFeature> oldFeatures = diffObject.getIntersect();
    List<ModelFeature> removeFeatures = diffObject.getExceptA();

    for (ModelFeature feature : removeFeatures) {
      this.uninstall(model, feature);
    }
    for (ModelFeature feature : oldFeatures) {
      this.reinstall(model, feature, modelService);
    }
    prevFeatures.addAll(newFeatures);
    for (ModelFeature feature : newFeatures) {
      this.preinstall(model, feature);
    }
    for (ModelFeature feature : newFeatures) {
      this.install(model, feature, modelService);
    }
    for (ModelFeature feature : newFeatures) {
      this.postinstall(model, feature);
    }
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

  @SafeVarargs
  private final <T> void merge(T dest, T... sources) {
    Class<?> entityClass = ClassUtil.getRealClass(dest);
    for (T source : sources) {
      mergeColumn(entityClass, dest, source);
    }
  }

  private <T> void mergeColumn(Class<?> entityClass, T dest, T source) {
    for (Field field : ClassUtil.getDeclaredFields(entityClass, Column.class)) {
      if (field.getAnnotation(Id.class) != null) {
        continue;
      }
      Object value = ognlUtil.getValue(field.getName(), source);
      if (value != null) {
        ClassUtil.setValue(dest, field.getName(), value);
      }
    }
    for (Field field : ClassUtil.getDeclaredFields(entityClass, OneToOne.class)) {
      if (StringUtil.isBlank(field.getAnnotation(OneToOne.class).mappedBy())) {
        continue;
      }
      Object target = ognlUtil.getValue(field.getName(), dest);
      Object value = ognlUtil.getValue(field.getName(), source);
      if (value != null) {
        merge(target, value);
      }
    }
  }

  @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
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
    HOLDER.remove();
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

  private List<Model> defaultTypes;

  public ModelUtilCache cache() {
    ModelUtilCache cache = HOLDER.get();
    if (cache == null) {
      if (defaultTypes == null) {
        defaultTypes = this.modelService.findAllByRoot();
      }
      HOLDER.set(new ModelUtilCache(defaultTypes));
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
      this.modelService.directSave(entity);
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
    private final Map<Long, Model> modelsById = new HashMap<>();
    private final Map<String, Model> modelsByCode = new HashMap<>();

    public ModelUtilCache() {}

    public ModelUtilCache(List<Model> types) {
      for (Model model : types) {
        this.putModel(model);
      }
    }

    public boolean containsModel(Long id) {
      return modelsById.containsKey(id);
    }

    public boolean containsModel(String code) {
      return modelsByCode.containsKey(code);
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

    public void putModel(Model model) {
      if (model.getId() != null) {
        this.modelsById.put(model.getId(), model);
      }
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
