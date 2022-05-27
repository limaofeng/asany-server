package cn.asany.shanhai.core.service;

import cn.asany.shanhai.core.dao.ModelFeatureDao;
import cn.asany.shanhai.core.domain.ModelFeature;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ModelFeatureService {

  @Autowired private ModelFeatureDao modelFeatureDao;

  public void saveOrUpdate(ModelFeature feature) {
    if (modelFeatureDao.existsById(feature.getId())) {
      modelFeatureDao.update(feature, true);
    } else {
      modelFeatureDao.save(feature);
    }
  }

  public void save(ModelFeature feature) {
    modelFeatureDao.save(feature);
  }

  public void clear() {
    modelFeatureDao.deleteAllInBatch();
  }

  public Optional<ModelFeature> get(String id) {
    return modelFeatureDao.findById(id);
  }
}
