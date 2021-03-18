package cn.asany.shanhai.core.service;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.ModelFeature;
import cn.asany.shanhai.core.bean.ModelField;
import cn.asany.shanhai.core.bean.ModelMetadata;
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
    private RuntimeMetadataRegistry metadataRegistry;
    @Autowired
    private HibernateMappingHelper hibernateMappingHelper;
    @Autowired
    private RuntimeJpaRepositoryFactory jpaRepositoryFactory;
    @Autowired
    private ModelFeatureService modelFeatureService;
    @Autowired
    private ModelFeatureRegistry modelFeatureRegistry;

    public Pager<Model> findPager(Pager<Model> pager, List<PropertyFilter> filters) {
        return modelDao.findPager(pager, filters);
    }

    @SneakyThrows
    public Model save(Model model) {
        // 检查 Model Metadata 设置
        ModelUtils.inject(model);

        if (model.getType() == ModelType.SCALAR) {
            return modelDao.save(model);
        }

        // 检查主键
        Optional<ModelField> idFieldOptional = ModelUtils.getId(model);
        if (!idFieldOptional.isPresent()) {
            model.getFields().add(0, ModelUtils.generatePrimaryKeyField());
        }

        // 特征处理
        for (ModelFeature feature : model.getFeatures()) {
            Optional<ModelFeature> optionalModelFeature = modelFeatureService.get(feature.getId());
            if (!optionalModelFeature.isPresent()) {
                throw new ValidationException("0000000", String.format("模型特征[%s]不存在", feature.getId()));
            }
            ModelUtils.inject(model, modelFeatureRegistry.get(feature.getId()));
        }

        // 检查 ModelField Metadata 设置
        for (ModelField field : model.getFields()) {
            ModelUtils.inject(field);
            field.setModel(model);
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
        modelDao.update(model, false);
    }

    public Optional<Model> get(Long id) {
        return modelDao.findById(id);
    }

    public void clear() {
        List<Model> models = this.modelDao.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        this.modelDao.deleteAll(models);
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
}
