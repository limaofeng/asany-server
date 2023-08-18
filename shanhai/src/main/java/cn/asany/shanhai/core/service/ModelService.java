package cn.asany.shanhai.core.service;

import cn.asany.shanhai.core.dao.*;
import cn.asany.shanhai.core.domain.*;
import cn.asany.shanhai.core.domain.enums.ModelRelationType;
import cn.asany.shanhai.core.domain.enums.ModelStatus;
import cn.asany.shanhai.core.domain.enums.ModelType;
import cn.asany.shanhai.core.event.*;
import cn.asany.shanhai.core.support.model.features.MasterModelFeature;
import cn.asany.shanhai.core.utils.DuplicateFieldException;
import cn.asany.shanhai.core.utils.ModelUtils;
import cn.asany.shanhai.core.utils.TypeNotFoundException;
import cn.asany.shanhai.gateway.domain.ModelGroupItem;
import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.util.JdbcUtils;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 模型服务
 *
 * @author limaofeng
 */
@Slf4j
@Service
public class ModelService {
  private final ModelDao modelDao;
  private final ModuleDao moduleDao;
  private final ModelFieldDao modelFieldDao;
  private final ModelEndpointDao modelEndpointDao;
  private final ModelRelationDao modelRelationDao;
  private final ModelMetadataDao modelMetadataDao;
  private final ModelFieldMetadataDao modelFieldMetadataDao;
  private final ModelFieldArgumentDao modelFieldArgumentDao;
  private final ModelFeatureDao modelFeatureDao;
  public static final String CACHE_KEY = "MODEL";

  private final ApplicationEventPublisher publisher;

  private final CacheManager cacheManager;

  public ModelService(
      CacheManager cacheManager,
      ModelDao modelDao,
      ModuleDao moduleDao,
      ModelFieldDao modelFieldDao,
      ModelEndpointDao modelEndpointDao,
      ModelRelationDao modelRelationDao,
      ModelFeatureDao modelFeatureDao,
      ModelFieldArgumentDao modelFieldArgumentDao,
      ModelMetadataDao modelMetadataDao,
      ModelFieldMetadataDao modelFieldMetadataDao,
      ApplicationEventPublisher publisher) {
    this.cacheManager = cacheManager;
    this.modelDao = modelDao;
    this.moduleDao = moduleDao;
    this.modelFieldDao = modelFieldDao;
    this.modelEndpointDao = modelEndpointDao;
    this.modelRelationDao = modelRelationDao;
    this.modelFeatureDao = modelFeatureDao;
    this.modelFieldArgumentDao = modelFieldArgumentDao;
    this.modelMetadataDao = modelMetadataDao;
    this.modelFieldMetadataDao = modelFieldMetadataDao;
    this.publisher = publisher;
  }

  public Page<Model> findPage(Pageable pageable, PropertyFilter filter) {
    return modelDao.findPage(pageable, filter);
  }

  public List<Model> findAll(PropertyFilter filter, int offset, int limit, Sort orderBy) {
    return modelDao.findAll(filter, offset, limit, orderBy);
  }

  @Transactional(isolation = Isolation.READ_COMMITTED)
  public Model save(Model model) {
    model.setModule(this.moduleDao.getReferenceById(model.getModule().getId()));
    ModelUtils modelUtils = ModelUtils.getInstance();
    model = save(model, modelUtils);
    publisher.publishEvent(new CreateModelEvent(model));
    return model;
  }

  @SneakyThrows
  public Model save(Model model, ModelUtils modelUtils) {
    // 检查 Model Metadata 设置
    modelUtils.initialize(model);

    Set<ModelField> fields = model.getFields();
    Set<ModelEndpoint> endpoints = model.getEndpoints();
    Set<ModelFeature> features = model.getFeatures();

    if (model.getType() == ModelType.SCALAR) {
      return modelUtils.saveWithCache(model);
    }

    modelUtils.saveWithCache(model);

    // 检查 ModelField Metadata 设置
    Iterator<ModelField> iterator = fields.iterator();
    while (iterator.hasNext()) {
      ModelField field = iterator.next();
      try {
        modelUtils.install(model, field);
      } catch (TypeNotFoundException e) {
        if (!modelUtils.isLazySave()) {
          throw e;
        }
        modelUtils.lazySave(field);
        iterator.remove();
      }
    }

    // 检查设置 Endpoint
    for (ModelEndpoint endpoint : endpoints) {
      modelUtils.install(model, endpoint);
    }

    if (model.getType() != ModelType.ENTITY) {
      return modelUtils.saveWithCache(model);
    }

    // 检查主键
    Optional<ModelField> idFieldOptional = modelUtils.getId(model);
    if (!idFieldOptional.isPresent()) {
      ModelField idField = modelUtils.generatePrimaryKeyField();
      model.getFields().add(modelUtils.install(model, idField));
    }

    features =
        features.stream()
            .map(
                f ->
                    modelFeatureDao
                        .findById(f.getId())
                        .orElseThrow(
                            () ->
                                new ValidationException(
                                    "0000000", String.format("模型特征[%s]不存在", f.getId()))))
            .collect(Collectors.toSet());
    model.setFeatures(features);

    // 特征处理
    for (ModelFeature feature : features) {
      modelUtils.preinstall(model, feature);
    }
    for (ModelFeature feature : features) {
      modelUtils.install(model, feature, this);
    }
    for (ModelFeature feature : features) {
      modelUtils.postinstall(model, feature);
    }

    // 检查 ModelRelation 设置
    if (model.getRelations() != null) {
      for (ModelRelation relation : model.getRelations()) {
        if (relation.getModel() != model) {
          relation.setModel(model);
        }
      }
    }

    return modelUtils.saveWithCache(model);
  }

  public void saveAllInBatch(List<Model> models) {
    ModelUtils modelUtils = ModelUtils.getInstance();
    modelUtils.clear();

    modelUtils.enableLazySave();

    // 去掉默认类型
    models =
        models.stream()
            .filter(item -> !ObjectUtil.exists(ModelUtils.DEFAULT_SCALARS, "code", item.getCode()))
            .collect(Collectors.toList());

    List<ModelField> queries = new ArrayList<>();
    List<ModelField> mutations = new ArrayList<>();

    for (int i = 0; i < models.size(); i++) {
      Model model = models.get(i);
      if ("Query".equals(model.getCode())) {
        queries.addAll(model.getFields());
      } else if ("Mutation".equals(model.getCode())) {
        mutations.addAll(model.getFields());
      } else {
        try {
          this.save(model, modelUtils);
        } catch (TypeNotFoundException e) {
          models.remove(i);
          i--;
          models.add(model);
        }
      }
    }

    ModelUtils.ModelLazySaveContext context = modelUtils.getModelLazySaveContext();

    Model query =
        modelUtils
            .getModelByCode("Query")
            .orElseThrow(() -> new ValidationException("Model Query 未找到"));
    Model mutation =
        modelUtils
            .getModelByCode("Mutation")
            .orElseThrow(() -> new ValidationException("Model Mutation 未找到"));

    this.modelDao.saveAllInBatch(context.saves);
    this.modelFieldDao.saveAllInBatch(
        context.savesByField.stream()
            .map(item -> modelUtils.install(item.getModel(), item))
            .collect(Collectors.toList()));
    this.modelFieldDao.saveAllInBatch(
        queries.stream().map(item -> modelUtils.install(query, item)).collect(Collectors.toList()));
    this.modelFieldDao.saveAllInBatch(
        mutations.stream()
            .map(item -> modelUtils.install(mutation, item))
            .collect(Collectors.toList()));

    modelUtils.clear();
    modelUtils.disableLazySave();
  }

  @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
  public Model update(Long id, Model input) {
    Model model = this.modelDao.getReferenceById(id);

    String tableName = model.getMetadata().getDatabaseTableName();
    String newTableName = input.getMetadata().getDatabaseTableName();

    boolean codeChanged = !input.getCode().equals(model.getCode());
    boolean nameChanged = !input.getName().equals(model.getName());

    model.setCode(input.getCode());
    model.setName(input.getName());
    model.setDescription(input.getDescription());
    model.getMetadata().setDatabaseTableName(newTableName);

    if (codeChanged || nameChanged) {
      Set<ModelFeature> features = model.getFeatures();
      ModelUtils modelUtils = ModelUtils.getInstance();
      for (ModelFeature feature : features) {
        modelUtils.preinstall(model, feature);
      }
      for (ModelFeature feature : features) {
        modelUtils.reinstall(model, feature, this);
      }
      for (ModelFeature feature : features) {
        modelUtils.postinstall(model, feature);
      }
    }

    if (!tableName.equals(newTableName)) {
      JdbcUtils.renameTable(tableName, newTableName);
    }

    publisher.publishEvent(new UpdateModelEvent(model));
    return model;
  }

  public Model update(Model model) {
    this.modelDao.getEntityManager().detach(model);
    ModelUtils modelUtils = ModelUtils.getInstance();

    if (ObjectUtil.isNull(model.getId()) && StringUtil.isBlank(model.getCode())) {
      throw new ValidationException("Model 的 [ ID, CODE ] 不能同时为空");
    }
    Optional<Model> optionalOriginal =
        model.getId() != null
            ? modelUtils.getModelById(model.getId())
            : modelUtils.getModelByCode(model.getCode());
    if (!optionalOriginal.isPresent()) {
      throw new ValidationException(
          "Model 的 [ ID = " + model.getId() + ", CODE = " + model.getCode() + " ] 没有查询到对应的数据");
    }
    Model original = optionalOriginal.get();

    original.setCode(model.getCode());
    original.setName(model.getName());

    // 合并字段
    modelUtils.mergeFields(original, model.getFields());

    if (model.getType() != ModelType.ENTITY) {
      return this.modelDao.update(original);
    }

    // 合并特征
    modelUtils.mergeFeatures(original, model.getFeatures(), this);

    return this.modelDao.update(original);
  }

  public void publish(Long id) {
    Model model = modelDao.getReferenceById(id);
    //        ModelMetadata metadata = model.getMetadata();
    //        String xml = hibernateMappingHelper.generateXML(model);
    //        metadata.setHbm(xml);
    model.setStatus(ModelStatus.PUBLISHED);
    this.modelDao.update(model);
  }

  public Model getById(Long id) {
    return this.modelDao.getReferenceById(id);
  }

  public Optional<Model> findById(Long id) {
    Cache cache = this.cacheManager.getCache(CACHE_KEY);
    assert cache != null;
    return Optional.ofNullable(
        cache.get(
            this.getClass().getName() + ".findById#" + id, () -> this.modelDao.findById(id).get()));
  }

  public int delete(Long[] ids) {
    for (Long id : ids) {
      this.modelDao.delete(this.modelDao.getReferenceById(id));
    }
    return ids.length;
  }

  @Transactional
  public void delete(Long id) {
    Model model = this.modelDao.getReferenceById(id);

    Set<ModelEndpoint> endpoints = model.getEndpoints();
    this.modelEndpointDao.deleteAll(endpoints);

    Set<ModelRelation> relations = model.getRelations();
    for (ModelRelation relation : relations) {
      Model inverse = relation.getInverse();
      this.modelRelationDao.delete(relation);
      this.modelDao.delete(inverse);
    }

    this.modelDao.delete(model);
    this.publisher.publishEvent(new DeleteModelEvent(model));
  }

  private void aggregate(
      Set<ModelField> queries,
      Set<ModelField> mutations,
      Set<Long> apiFieldIds,
      Set<Long> apiFieldArgumentIds) {
    for (ModelField field : queries) {
      apiFieldIds.add(field.getId());
      apiFieldArgumentIds.addAll(
          field.getArguments().stream().map(ModelFieldArgument::getId).collect(Collectors.toSet()));
    }
    for (ModelField field : mutations) {
      apiFieldIds.add(field.getId());
      apiFieldArgumentIds.addAll(
          field.getArguments().stream().map(ModelFieldArgument::getId).collect(Collectors.toSet()));
    }
  }

  private void aggregate(
      List<Model> models, Set<Long> modelIds, Set<Long> fieldIds, Set<Long> argumentIds) {
    models.forEach(
        model -> {
          modelIds.add(model.getId());
          aggregate(model, fieldIds, argumentIds);
        });
  }

  private void aggregate(Model model, Set<Long> fieldIds, Set<Long> argumentIds) {
    model
        .getFields()
        .forEach(
            field -> {
              fieldIds.add(field.getId());
              argumentIds.addAll(
                  field.getArguments().stream()
                      .map(ModelFieldArgument::getId)
                      .collect(Collectors.toSet()));
            });
  }

  public void delete(Model model) {
    if (model.getType() != ModelType.ENTITY) {
      Set<Long> fieldIds = new HashSet<>();
      Set<Long> argumentIds = new HashSet<>();
      aggregate(model, fieldIds, argumentIds);
      this.modelFieldDao.deleteAllByIdInBatch(fieldIds);
      List<Model> ids = new ArrayList<>();
      ids.add(model);
      this.modelDao.deleteAllInBatch(ids);
      log.debug("删除" + model.getCode());
      return;
    }
    this.modelEndpointDao.deleteAll(model.getEndpoints());
    List<Model> types =
        model.getRelations().stream()
            .filter(item -> item.getType() == ModelRelationType.SUBJECTION)
            .map(ModelRelation::getInverse)
            .collect(Collectors.toList());
    this.modelRelationDao.deleteAll(model.getRelations());
    this.modelDao.deleteAll(types);
    this.modelDao.delete(model);
  }

  public void clear() {
    PropertyFilter filter = PropertyFilter.newFilter();
    List<Model> models =
        this.modelDao.findAllWithMetadataAndFields(
            filter, Sort.by(Sort.Direction.DESC, "createdAt"));
    Set<Long> modelIds = new HashSet<>();
    Set<Long> fieldIds = new HashSet<>();
    Set<Long> argumentIds = new HashSet<>();

    Set<Long> apiFieldIds = new HashSet<>();
    Set<Long> apiFieldArgumentIds = new HashSet<>();

    Set<ModelField> queries = ObjectUtil.find(models, "code", "Query").getFields();
    Set<ModelField> mutations = ObjectUtil.find(models, "code", "Mutation").getFields();

    models =
        models.stream()
            .filter(item -> !ObjectUtil.exists(ModelUtils.DEFAULT_TYPES, "id", item.getId()))
            .collect(Collectors.toList());

    aggregate(models, modelIds, fieldIds, argumentIds);
    aggregate(queries, mutations, apiFieldIds, apiFieldArgumentIds);

    if (!apiFieldArgumentIds.isEmpty()) {
      this.modelFieldArgumentDao.deleteAllByIdInBatch(apiFieldArgumentIds);
    }
    if (!apiFieldIds.isEmpty()) {
      this.modelFieldDao.deleteAllByIdInBatch(apiFieldIds);
    }

    if (!argumentIds.isEmpty()) {
      this.modelFieldArgumentDao.deleteAllByIdInBatch(argumentIds);
    }
    if (!fieldIds.isEmpty()) {
      this.modelFieldMetadataDao.deleteAllByIdInBatch(fieldIds);
      this.modelFieldDao.deleteAllByIdInBatch(fieldIds);
    }
    if (!modelIds.isEmpty()) {
      this.modelMetadataDao.deleteAllByIdInBatch(modelIds);
      this.modelDao.deleteAllByIdInBatch(modelIds);
    }
    System.out.println("....");
  }

  @Transactional(readOnly = true)
  public List<Model> findEntityModels() {
    Cache cache = this.cacheManager.getCache(CACHE_KEY);
    assert cache != null;
    return cache.get(
        this.getClass().getName() + ".findEntityModels",
        () -> this.modelDao.findAll(PropertyFilter.newFilter().equal("type", ModelType.ENTITY)));
  }

  @Transactional(readOnly = true)
  public Model getDetails(Long id) {
    Cache cache = this.cacheManager.getCache(CACHE_KEY);
    assert cache != null;
    return cache.get(
        this.getClass().getName() + ".getDetails#" + id, () -> this.modelDao.getDetails(id));
  }

  @Transactional
  public void saveInBatch(Model... models) {
    for (Model model : models) {
      if (this.modelDao.existsById(model.getId())) {
        this.modelDao.update(model, true);
      } else {
        this.save(model);
      }
    }
  }

  public Optional<Model> findByCode(String code) {
    Cache cache = this.cacheManager.getCache(CACHE_KEY);
    assert cache != null;
    return Optional.ofNullable(
        cache.get(
            this.getClass().getName() + ".findByCode#" + code,
            () -> this.modelDao.findOne(PropertyFilter.newFilter().equal("code", code)).get()));
  }

  public boolean exists(String code) {
    ModelUtils modelUtils = ModelUtils.getInstance();
    return modelUtils.getModelByCode(code).isPresent();
  }

  @SneakyThrows
  public void save(ModelField field) {
    ModelUtils modelUtils = ModelUtils.getInstance();
    modelFieldDao.save(modelUtils.install(field.getModel(), field));
  }

  public List<ModelField> queries() {
    PropertyFilter filter = PropertyFilter.newFilter();
    filter.equal("model.code", "Query");
    return this.modelFieldDao.findAll(filter);
  }

  public List<ModelField> findAllModelFields(PropertyFilter filter) {
    return this.modelFieldDao.findAll(filter);
  }

  public List<ModelField> endpoints() {
    PropertyFilter filter = PropertyFilter.newFilter();
    filter.in("model.code", "Query", "Mutation");
    return this.modelFieldDao.findWithModelAndType(filter);
  }

  public List<ModelGroupItem> findResourcesByUngrouped() {
    List<ModelGroupItem> items = new ArrayList<>();
    int i = 0;
    for (ModelField field : this.modelFieldDao.findByUngrouped()) {
      items.add(
          ModelGroupItem.builder()
              .id((long) --i)
              .resourceId(field.getId())
              .resourceType("ENDPOINT")
              .resource(field)
              .build());
    }
    for (Model model : this.modelDao.findByUngrouped()) {
      items.add(
          ModelGroupItem.builder()
              .id((long) --i)
              .resourceId(model.getId())
              .resourceType("MODEL")
              .resource(model)
              .build());
    }
    return items;
  }

  public Optional<ModelField> findEndpointById(Long id) {
    return this.modelFieldDao.findById(id);
  }

  public Optional<ModelField> findEndpointByCode(String code) {
    PropertyFilter filter = PropertyFilter.newFilter();
    filter.equal("code", code);
    return this.modelFieldDao.findOne(filter);
  }

  @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
  public ModelField addField(Long modelId, ModelField field) {
    ModelUtils modelUtils = ModelUtils.getInstance();
    Model model = this.modelDao.getReferenceById(modelId);
    if (ObjectUtil.exists(model.getFields(), "code", field.getCode())) {
      throw new DuplicateFieldException("code", field.getCode());
    }
    if (ObjectUtil.exists(model.getFields(), "name", field.getName())) {
      throw new DuplicateFieldException("name", field.getCode());
    }
    String databaseColumnName =
        field.getMetadata() == null ? null : field.getMetadata().getDatabaseColumnName();
    if (databaseColumnName != null
        && ObjectUtil.exists(model.getFields(), "DatabaseColumnName", databaseColumnName)) {
      throw new DuplicateFieldException("name", databaseColumnName);
    }
    ModelField newField = modelUtils.install(model, field);
    this.modelFieldDao.save(newField);
    this.modelDao.save(model);
    Set<ModelFeature> features = model.getFeatures();
    // 特征处理
    for (ModelFeature feature : features) {
      modelUtils.reinstall(model, feature, this);
    }
    publisher.publishEvent(new CreateModelFieldEvent(modelId, newField));
    return newField;
  }

  @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
  public ModelField updateField(Long id, Long fieldId, ModelField field) {
    ModelUtils modelUtils = ModelUtils.getInstance();
    field.setId(fieldId);
    Model model = this.modelDao.getReferenceById(id);
    ModelField source =
        model.getFields().stream()
            .filter(f -> fieldId.equals(f.getId()))
            .findAny()
            .orElseThrow(() -> new ValidationException("修改的字段[ID=" + id + "." + fieldId + "]不存在"));

    String oldColumnName = source.getMetadata().getDatabaseColumnName();
    String newColumnName = field.getMetadata().getDatabaseColumnName();

    if (ObjectUtil.exists(model.getFields(), "code", field.getCode())
        && !source.getCode().equals(field.getCode())) {
      throw new DuplicateFieldException("code", field.getCode());
    }
    if (ObjectUtil.exists(model.getFields(), "name", field.getName())
        && !source.getName().equals(field.getName())) {
      throw new DuplicateFieldException("name", field.getCode());
    }
    String databaseColumnName =
        field.getMetadata() == null ? null : field.getMetadata().getDatabaseColumnName();
    if (databaseColumnName != null
        && ObjectUtil.exists(model.getFields(), "DatabaseColumnName", databaseColumnName)
        && !source
            .getMetadata()
            .getDatabaseColumnName()
            .equals(field.getMetadata().getDatabaseColumnName())) {
      throw new DuplicateFieldException("name", databaseColumnName);
    }
    modelUtils.reinstall(model, field);
    this.modelDao.save(model);
    Set<ModelFeature> features = model.getFeatures();
    // 特征处理
    for (ModelFeature feature : features) {
      modelUtils.reinstall(model, feature, this);
    }

    if (!oldColumnName.equals(newColumnName)) {
      JdbcUtils.renameColumn(
          model.getMetadata().getDatabaseTableName(), oldColumnName, newColumnName);
    }

    publisher.publishEvent(new UpdateModelFieldEvent(model.getId(), field));
    return source;
  }

  @Transactional(readOnly = true)
  public List<ModelField> listModelFields(PropertyFilter filter, int offset, int limit, Sort sort) {
    return this.modelFieldDao.findAll(filter, offset, limit, sort);
  }

  @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
  public void removeField(Long id, Long fieldId) {
    Model model = this.modelDao.getReferenceById(id);
    ModelField field = ObjectUtil.remove(model.getFields(), "id", fieldId);
    Set<ModelRelation> relations = model.getRelations();
    List<ModelField> allDeleteFields = new ArrayList<>();
    for (ModelRelation relation : relations) {
      Model inverse = relation.getInverse();
      boolean isWhereType = inverse.getCode().endsWith(MasterModelFeature.SUFFIX_INPUT_WHERE);
      boolean isUpdateType = inverse.getCode().endsWith(MasterModelFeature.SUFFIX_INPUT_UPDATE);
      boolean isCreateType = inverse.getCode().endsWith(MasterModelFeature.SUFFIX_INPUT_CREATE);
      if (inverse.getType() == ModelType.INPUT_OBJECT) {
        if (isWhereType) {
          allDeleteFields.addAll(
              inverse.getFields().stream()
                  .filter(
                      item ->
                          item.getCode().startsWith(field.getCode() + "_")
                              || item.getCode().equals(field.getCode()))
                  .collect(Collectors.toList()));
        } else if (isUpdateType || isCreateType) {
          allDeleteFields.addAll(
              inverse.getFields().stream()
                  .filter(item -> item.getCode().equals(field.getCode()))
                  .collect(Collectors.toList()));
        }
      } else if (inverse.getType() == ModelType.ENUM) {
        allDeleteFields.addAll(
            inverse.getFields().stream()
                .filter(item -> item.getCode().startsWith(field.getCode() + "_"))
                .collect(Collectors.toList()));
      }
      allDeleteFields.forEach(inverse.getFields()::remove);
    }
    allDeleteFields.add(field);
    this.modelFieldDao.deleteAll(allDeleteFields);
    this.publisher.publishEvent(new DeleteModelFieldEvent(id, field));
  }

  public EntityManager getEntityManager() {
    return this.modelDao.getEntityManager();
  }

  public List<Model> findAllByRoot() {
    Cache cache = cacheManager.getCache(CACHE_KEY);
    assert cache != null;
    return cache.get(
        this.getClass().getName() + ".findAllByRoot",
        () ->
            this.modelDao.findAll(
                PropertyFilter.newFilter()
                    .or(
                        PropertyFilter.newFilter().equal("type", ModelType.SCALAR),
                        PropertyFilter.newFilter().in("code", "Query", "Mutation"))));
  }

  public void directSave(Model entity) {
    this.modelDao.save(entity);
  }
}
