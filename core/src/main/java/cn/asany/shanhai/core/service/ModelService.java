package cn.asany.shanhai.core.service;

import cn.asany.shanhai.core.bean.*;
import cn.asany.shanhai.core.bean.enums.ModelConnectType;
import cn.asany.shanhai.core.bean.enums.ModelRelationType;
import cn.asany.shanhai.core.bean.enums.ModelStatus;
import cn.asany.shanhai.core.bean.enums.ModelType;
import cn.asany.shanhai.core.dao.*;
import cn.asany.shanhai.core.support.dao.ModelRepositoryFactory;
import cn.asany.shanhai.core.support.model.ModelFeatureRegistry;
import cn.asany.shanhai.core.utils.HibernateMappingHelper;
import cn.asany.shanhai.core.utils.ModelUtils;
import lombok.SneakyThrows;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ModelService {
    @Autowired
    private ModelDao modelDao;
    @Autowired
    private ModelMetadataDao modelMetadataDao;
    @Autowired
    private ModelFieldDao modelFieldDao;
    @Autowired
    private ModelFieldMetadataDao modelFieldMetadataDao;
    @Autowired
    private ModelEndpointDao modelEndpointDao;
    @Autowired
    private ModelRelationDao modelRelationDao;
    //    @Autowired
//    private RuntimeMetadataRegistry metadataRegistry;
    @Autowired
    private ModelRepositoryFactory jpaRepositoryFactory;
    @Autowired
    private ModelFeatureService modelFeatureService;
    @Autowired
    private ModelFeatureRegistry modelFeatureRegistry;
    @Autowired
    public ModelUtils modelUtils;

    public Pager<Model> findPager(Pager<Model> pager, List<PropertyFilter> filters) {
        return modelDao.findPager(pager, filters);
    }

    @SneakyThrows
    public Model save(Model model) {
        // 检查 Model Metadata 设置
        modelUtils.initialize(model);

        if (model.getType() == ModelType.SCALAR) {
            return modelDao.save(model);
        }

        // 检查 ModelField Metadata 设置
        for (ModelField field : model.getFields()) {
            modelUtils.install(model, field);
        }

        if (model.getType() == ModelType.INPUT) {
            return modelDao.save(model);
        }

        // 检查主键
        Optional<ModelField> idFieldOptional = modelUtils.getId(model);
        if (!idFieldOptional.isPresent()) {
            ModelField idField = modelUtils.generatePrimaryKeyField();
            model.getFields().add(0, modelUtils.install(model, idField));
        }

        // 特征处理
        for (ModelFeature feature : model.getFeatures()) {
            Optional<ModelFeature> optionalModelFeature = modelFeatureService.get(feature.getId());
            if (!optionalModelFeature.isPresent()) {
                throw new ValidationException("0000000", String.format("模型特征[%s]不存在", feature.getId()));
            }
            modelUtils.install(model, feature);
        }

        // 检查设置 Endpoint
        for (ModelEndpoint endpoint : model.getEndpoints()) {
            modelUtils.install(model, endpoint);
        }

        // 检查 ModelRelation 设置
        for (ModelRelation relation : model.getRelations()) {
            relation.setModel(model);
        }

        return modelDao.save(model);
    }

    public Model update(Model model) {
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

        if (model.getType() != ModelType.OBJECT) {
            return this.modelDao.update(original);
        }

        // 合并特征
        modelUtils.mergeFeatures(original, model.getFeatures());

        return this.modelDao.update(original);
    }

    public void publish(Long id) {
        Model model = modelDao.getOne(id);
        ModelMetadata metadata = model.getMetadata();
//        String xml = hibernateMappingHelper.generateXML(model);
//        metadata.setHbm(xml);
        model.setStatus(ModelStatus.PUBLISHED);
        this.modelDao.update(model);
    }

    public Optional<Model> get(Long id) {
        return this.modelDao.findById(id);
    }

    public void delete(Long id) {
        this.modelDao.delete(this.modelDao.getOne(id));
    }

    public void delete(Model model) {
        if (model.getType() != ModelType.OBJECT) {
            this.delete(model);
            return;
        }
        for (ModelEndpoint endpoint : model.getEndpoints()) {
            this.modelEndpointDao.delete(endpoint);
        }
        List<Model> types = model.getRelations().stream().filter(item -> item.getType() == ModelRelationType.SUBJECTION).map(item -> item.getInverse()).collect(Collectors.toList());
        for (ModelRelation relation : model.getRelations()) {
            this.modelRelationDao.delete(relation);
        }
        for (Model type : types) {
            this.modelDao.delete(type);
        }
        this.modelDao.delete(model);
    }

    public void clear() {
        List<Model> models = this.modelDao.findAll(Example.of(Model.builder().type(ModelType.OBJECT).build()), Sort.by(Sort.Direction.DESC, "createdAt"));
        for (Model model : models) {
            this.delete(model);
        }
    }

    public List<Model> findAll() {
        return this.modelDao.findAll();
    }

    public List<Model> findAll(final ModelType... types) {
        return this.modelDao.findAll(PropertyFilter.builder().in("type", types).build());
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
        return this.modelDao.findOne(Example.of(Model.builder().code(code).build()));
    }

    public boolean exists(String code) {
        return this.modelDao.exists(Example.of(Model.builder().code(code).build()));
    }

}
