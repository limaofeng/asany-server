package cn.asany.shanhai.core.service;

import cn.asany.shanhai.core.dao.*;
import cn.asany.shanhai.core.domain.*;
import cn.asany.shanhai.core.domain.enums.ModelRelationType;
import cn.asany.shanhai.core.domain.enums.ModelStatus;
import cn.asany.shanhai.core.domain.enums.ModelType;
import cn.asany.shanhai.core.runners.InitModelDaoCommandLineRunner;
import cn.asany.shanhai.core.support.model.features.MasterModelFeature;
import cn.asany.shanhai.core.utils.DuplicateFieldException;
import cn.asany.shanhai.core.utils.ModelUtils;
import cn.asany.shanhai.core.utils.TypeNotFoundException;
import cn.asany.shanhai.gateway.domain.ModelGroupItem;
import java.util.*;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
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
  private final ModelFieldDao modelFieldDao;
  private final ModelEndpointDao modelEndpointDao;
  private final ModelRelationDao modelRelationDao;
  private final ModelFeatureService modelFeatureService;
  private final ModelMetadataDao modelMetadataDao;
  private final ModelFieldMetadataDao modelFieldMetadataDao;
  private final ModelFieldArgumentDao modelFieldArgumentDao;

  public ModelService(
      ModelDao modelDao,
      ModelFieldDao modelFieldDao,
      ModelEndpointDao modelEndpointDao,
      ModelRelationDao modelRelationDao,
      ModelFeatureService modelFeatureService,
      ModelFieldArgumentDao modelFieldArgumentDao,
      ModelMetadataDao modelMetadataDao,
      ModelFieldMetadataDao modelFieldMetadataDao) {
    this.modelDao = modelDao;
    this.modelFieldDao = modelFieldDao;
    this.modelEndpointDao = modelEndpointDao;
    this.modelRelationDao = modelRelationDao;
    this.modelFeatureService = modelFeatureService;
    this.modelFieldArgumentDao = modelFieldArgumentDao;
    this.modelMetadataDao = modelMetadataDao;
    this.modelFieldMetadataDao = modelFieldMetadataDao;
  }

  public Page<Model> findPage(Pageable pageable, List<PropertyFilter> filters) {
    return modelDao.findPage(pageable, filters);
  }

  public List<Model> findAll(List<PropertyFilter> filters, int offset, int limit, Sort orderBy) {
    return modelDao.findAll(filters, offset, limit, orderBy);
  }

  @Transactional(isolation = Isolation.READ_COMMITTED)
  public Model save(Model model) {
    ModelUtils modelUtils = ModelUtils.getInstance();
    return save(model, modelUtils);
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
                    modelFeatureService
                        .get(f.getId())
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
      modelUtils.install(model, feature);
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

    List<Model> types =
        this.modelDao.findAllByTypesWithMetadataAndFields(
            ModelType.OBJECT,
            ModelType.SCALAR,
            ModelType.INPUT_OBJECT,
            ModelType.ENUM,
            ModelType.INTERFACE,
            ModelType.UNION);
    modelUtils.newCache(types);
    modelUtils.enableLazySave();

    long start = System.currentTimeMillis();
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

    String x = InitModelDaoCommandLineRunner.fromNow(start);
    System.out.println("总耗时: " + x);

    ModelUtils.ModelLazySaveContext context = modelUtils.getModelLazySaveContext();

    long x_start = System.currentTimeMillis();

    Model Query =
        modelUtils
            .getModelByCode("Query")
            .orElseThrow(() -> new ValidationException("Model Query 未找到"));
    Model Mutation =
        modelUtils
            .getModelByCode("Mutation")
            .orElseThrow(() -> new ValidationException("Model Mutation 未找到"));

    this.modelDao.saveAllInBatch(context.saves);
    this.modelFieldDao.saveAllInBatch(
        context.savesByField.stream()
            .map(item -> modelUtils.install(item.getModel(), item))
            .collect(Collectors.toList()));
    this.modelFieldDao.saveAllInBatch(
        queries.stream().map(item -> modelUtils.install(Query, item)).collect(Collectors.toList()));
    this.modelFieldDao.saveAllInBatch(
        mutations.stream()
            .map(item -> modelUtils.install(Mutation, item))
            .collect(Collectors.toList()));
    String ss = InitModelDaoCommandLineRunner.fromNow(x_start);
    System.out.println("批量保存耗时: " + ss);

    modelUtils.clear();
    modelUtils.disableLazySave();
  }

  public Model update(Model model) {
    ModelUtils modelUtils = ModelUtils.getInstance();
    modelUtils.initialize(model);

    if (ObjectUtil.isNull(model.getId()) && StringUtil.isBlank(model.getCode())) {
      throw new ValidationException("Model 的 [ ID, CODE ] 不能同时为空");
    }
    Optional<Model> optionalOriginal =
        model.getId() != null ? modelDao.findById(model.getId()) : findByCode(model.getCode());
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
    modelUtils.mergeFeatures(original, model.getFeatures());

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

  public Optional<Model> get(Long id) {
    return this.modelDao.findById(id);
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
    PropertyFilterBuilder builder = PropertyFilter.builder();
    List<Model> models =
        this.modelDao.findAllWithMetadataAndFields(
            builder.build(), Sort.by(Sort.Direction.DESC, "createdAt"));
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

  public List<Model> findAll() {
    return this.modelDao.findAll();
  }

  public List<Model> findAll(ModelType... types) {
    return this.modelDao.findAllByTypesWithMetadataAndFields(types);
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
    ModelUtils modelUtils = ModelUtils.getInstance();
    return modelUtils.getModelByCode(code);
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
    PropertyFilterBuilder builder = PropertyFilter.builder();
    builder.equal("model.code", "Query");
    return this.modelFieldDao.findAll(builder.build());
  }

  public List<ModelField> findAllModelFields(List<PropertyFilter> filters) {
    return this.modelFieldDao.findAll(filters);
  }

  public List<ModelField> endpoints() {
    PropertyFilterBuilder builder = PropertyFilter.builder();
    builder.in("model.code", "Query", "Mutation");
    return this.modelFieldDao.findWithModelAndType(builder.build());
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
    PropertyFilterBuilder builder = PropertyFilter.builder();
    builder.equal("code", code);
    return this.modelFieldDao.findOne(builder.build());
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
      modelUtils.reinstall(model, feature);
    }
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
      modelUtils.reinstall(model, feature);
    }
    return source;
  }

  @Transactional(readOnly = true)
  public List<ModelField> listModelFields(
      List<PropertyFilter> filters, int offset, int limit, Sort sort) {
    return this.modelFieldDao.findAll(filters, offset, limit, sort);
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
  }
}
