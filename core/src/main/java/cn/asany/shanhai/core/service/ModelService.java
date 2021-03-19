package cn.asany.shanhai.core.service;

import cn.asany.shanhai.core.bean.*;
import cn.asany.shanhai.core.bean.enums.ModelConnectType;
import cn.asany.shanhai.core.bean.enums.ModelStatus;
import cn.asany.shanhai.core.bean.enums.ModelType;
import cn.asany.shanhai.core.dao.*;
import cn.asany.shanhai.core.support.RuntimeJpaRepositoryFactory;
import cn.asany.shanhai.core.support.model.ModelFeatureRegistry;
import cn.asany.shanhai.core.support.model.RuntimeMetadataRegistry;
import cn.asany.shanhai.core.utils.HibernateMappingHelper;
import cn.asany.shanhai.core.utils.ModelUtils;
import lombok.SneakyThrows;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.error.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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
    @Autowired
    private RuntimeMetadataRegistry metadataRegistry;
    @Autowired
    private HibernateMappingHelper hibernateMappingHelper;
    @Autowired
    private RuntimeJpaRepositoryFactory jpaRepositoryFactory;
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
        modelUtils.inject(model);

        if (model.getType() == ModelType.SCALAR) {
            return modelDao.save(model);
        }

        // 检查主键
        Optional<ModelField> idFieldOptional = modelUtils.getId(model);
        if (model.getType() == ModelType.OBJECT && !idFieldOptional.isPresent()) {
            model.getFields().add(0, modelUtils.generatePrimaryKeyField());
        }

        // 特征处理
        for (ModelFeature feature : model.getFeatures()) {
            Optional<ModelFeature> optionalModelFeature = modelFeatureService.get(feature.getId());
            if (!optionalModelFeature.isPresent()) {
                throw new ValidationException("0000000", String.format("模型特征[%s]不存在", feature.getId()));
            }
            modelUtils.inject(model, modelFeatureRegistry.get(feature.getId()));
        }

        // 检查 ModelField Metadata 设置
        for (ModelField field : model.getFields()) {
            modelUtils.inject(model, field);
        }

        // 检查 ModelRelation 设置
        for (ModelRelation relation : model.getRelations()) {
            relation.setModel(model);
        }

        return modelDao.save(model);
    }

    public void publish(Long id) {
        Model model = modelDao.getOne(id);
        ModelMetadata metadata = model.getMetadata();
        String xml = hibernateMappingHelper.generateXML(model);
        metadataRegistry.addMapping(xml);
        metadata.setHbm(xml);
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
        List<Model> inputTypes = model.getRelations().stream().filter(item -> item.getType() == ModelConnectType.INPUT.type && ModelConnectType.INPUT.relation.equals(item.getRelation())).map(item -> item.getInverse()).collect(Collectors.toList());
        for (ModelRelation relation : model.getRelations()) {
            this.modelRelationDao.delete(relation);
        }
        for (Model type : inputTypes) {
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

    public void saveInBatch(Model... models) {
        for (Model model : models) {
            this.save(model);
        }
    }

    public Optional<Model> findByCode(String code) {
        return this.modelDao.findOne(Example.of(Model.builder().code(code).build()));
    }

    public boolean exists(String code) {
        return this.modelDao.exists(Example.of(Model.builder().code(code).build()));
    }
}
