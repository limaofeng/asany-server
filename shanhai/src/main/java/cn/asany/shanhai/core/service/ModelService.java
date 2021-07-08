package cn.asany.shanhai.core.service;

import cn.asany.shanhai.core.bean.*;
import cn.asany.shanhai.core.bean.enums.ModelRelationType;
import cn.asany.shanhai.core.bean.enums.ModelStatus;
import cn.asany.shanhai.core.bean.enums.ModelType;
import cn.asany.shanhai.core.dao.*;
import cn.asany.shanhai.core.support.ModelSaveContext;
import cn.asany.shanhai.core.support.model.FieldTypeRegistry;
import cn.asany.shanhai.core.utils.FieldTypeNotFoundException;
import cn.asany.shanhai.core.utils.ModelUtils;
import cn.asany.shanhai.gateway.bean.ModelGroupItem;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ModelService {
    private final ModelDao modelDao;
    private final ModelFieldDao modelFieldDao;
    private final ModelEndpointDao modelEndpointDao;
    private final ModelRelationDao modelRelationDao;
    private final ModelFeatureService modelFeatureService;
    private final FieldTypeRegistry fieldTypeRegistry;
    private final ModelMetadataDao modelMetadataDao;
    private final ModelFieldMetadataDao modelFieldMetadataDao;
    private final ModelFieldArgumentDao modelFieldArgumentDao;

    public ModelService(ModelDao modelDao, ModelFieldDao modelFieldDao, ModelEndpointDao modelEndpointDao, ModelRelationDao modelRelationDao, ModelFeatureService modelFeatureService, FieldTypeRegistry fieldTypeRegistry, ModelFieldArgumentDao modelFieldArgumentDao, ModelMetadataDao modelMetadataDao, ModelFieldMetadataDao modelFieldMetadataDao) {
        this.modelDao = modelDao;
        this.modelFieldDao = modelFieldDao;
        this.modelEndpointDao = modelEndpointDao;
        this.modelRelationDao = modelRelationDao;
        this.modelFeatureService = modelFeatureService;
        this.fieldTypeRegistry = fieldTypeRegistry;
        this.modelFieldArgumentDao = modelFieldArgumentDao;
        this.modelMetadataDao = modelMetadataDao;
        this.modelFieldMetadataDao = modelFieldMetadataDao;
    }

    public Pager<Model> findPager(Pager<Model> pager, List<PropertyFilter> filters) {
        return modelDao.findPager(pager, filters);
    }

    @SneakyThrows
    public Model save(Model model) {
        // 检查 Model Metadata 设置
        ModelUtils modelUtils = ModelUtils.getInstance();
        modelUtils.initialize(model);

        if (model.getType() == ModelType.SCALAR) {
            return modelDao.save(model);
        }

        log.debug("实体" + model.getCode());

        // 检查 ModelField Metadata 设置
        Iterator<ModelField> iter = model.getFields().iterator();
        while (iter.hasNext()) {
            ModelField field = iter.next();
            try {
                modelUtils.install(model, field);
            } catch (FieldTypeNotFoundException e) {
                ModelSaveContext context = ModelSaveContext.getContext();
                if (context == null) {
                    throw e;
                }
                context.addField(field);
                iter.remove();
                log.error(e.getMessage());
            }
        }

        // 检查设置 Endpoint
        for (ModelEndpoint endpoint : model.getEndpoints()) {
            modelUtils.install(model, endpoint);
        }

        modelDao.save(model);

        if (model.getType() != ModelType.ENTITY) {
            return model;
        }

        // 检查主键
        Optional<ModelField> idFieldOptional = modelUtils.getId(model);
        if (!idFieldOptional.isPresent()) {
            ModelField idField = modelUtils.generatePrimaryKeyField();
            model.getFields().add(modelUtils.install(model, idField));
        }

        // 特征处理
        for (ModelFeature feature : model.getFeatures()) {
            Optional<ModelFeature> optionalModelFeature = modelFeatureService.get(feature.getId());
            if (!optionalModelFeature.isPresent()) {
                throw new ValidationException("0000000", String.format("模型特征[%s]不存在", feature.getId()));
            }
            modelUtils.install(model, feature);
        }

        // 检查 ModelRelation 设置
        for (ModelRelation relation : model.getRelations()) {
            relation.setModel(model);
        }

        Model result = modelDao.save(model);

        ModelSaveContext saveContext = ModelSaveContext.getContext();
        if (saveContext != null) {
            saveContext.getModels().add(result);
        }

        return result;
    }

    public Model update(Model model) {
        ModelUtils modelUtils = ModelUtils.getInstance();
        modelUtils.initialize(model);

        if (ObjectUtil.isNull(model.getId()) && StringUtil.isBlank(model.getCode())) {
            throw new ValidationException("Model 的 [ ID, CODE ] 不能同时为空");
        }
        Optional<Model> optionalOriginal = model.getId() != null ? modelDao.findById(model.getId()) : findByCode(model.getCode());
        if (!optionalOriginal.isPresent()) {
            throw new ValidationException("Model 的 [ ID = " + model.getId() + ", CODE = " + model.getCode() + " ] 没有查询到对应的数据");
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
        Model model = modelDao.getById(id);
        ModelMetadata metadata = model.getMetadata();
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
            this.modelDao.delete(this.modelDao.getOne(id));
        }
        return ids.length;
    }

    public void delete(Long id) {
        this.modelDao.delete(this.modelDao.getById(id));
    }

    private void aggregate(List<Model> models, Set<Long> modelIds, Set<Long> fieldIds, Set<Long> argumentIds) {
        models.stream().forEach(model -> {
            modelIds.add(model.getId());
            aggregate(model, fieldIds, argumentIds);
        });
    }

    private void aggregate(Model model, Set<Long> fieldIds, Set<Long> argumentIds) {
        model.getFields().forEach(field -> {
            fieldIds.add(field.getId());
            argumentIds.addAll(field.getArguments().stream().map(arg -> arg.getId()).collect(Collectors.toSet()));
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
        for (ModelEndpoint endpoint : model.getEndpoints()) {
            this.modelEndpointDao.delete(endpoint);
        }
        List<Model> types = model.getRelations().stream().filter(item -> item.getType() == ModelRelationType.SUBJECTION).map(ModelRelation::getInverse).collect(Collectors.toList());
        for (ModelRelation relation : model.getRelations()) {
            this.modelRelationDao.delete(relation);
        }
        for (Model type : types) {
            this.modelDao.delete(type);
        }
        this.modelDao.delete(model);
    }

    public void clear() {
        List<String> defaultScalars = fieldTypeRegistry.types().stream().map(item -> item.getId()).collect(Collectors.toList());
        PropertyFilterBuilder builder = PropertyFilter.builder();
        List<Model> models = this.modelDao.findAllWithMetadataAndFields(builder.build(), Sort.by(Sort.Direction.DESC, "createdAt"));
        Set<Long> modelIds = new HashSet<>();
        Set<Long> fieldIds = new HashSet<>();
        Set<Long> argumentIds = new HashSet<>();

        aggregate(models, modelIds, fieldIds, argumentIds);

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
        ModelSaveContext saveContext = ModelSaveContext.getContext();
        if (saveContext != null) {
            Model model = ObjectUtil.find(saveContext.getModels(), "code", code);
            return model != null ? Optional.of(model) : Optional.empty();
        }
        return this.modelDao.findOne(Example.of(Model.builder().code(code).build()));
    }

    public boolean exists(String code) {
        ModelSaveContext saveContext = ModelSaveContext.getContext();
        if (saveContext != null) {
            return ObjectUtil.exists(saveContext.getModels(), "code", code);
        }
        return this.modelDao.exists(Example.of(Model.builder().code(code).build()));
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
            items.add(ModelGroupItem.builder().id(Long.valueOf(--i)).resourceId(field.getId()).resourceType("ENDPOINT").resource(field).build());
        }
        for (Model model : this.modelDao.findByUngrouped()) {
            items.add(ModelGroupItem.builder().id(Long.valueOf(--i)).resourceId(model.getId()).resourceType("MODEL").resource(model).build());
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
}
