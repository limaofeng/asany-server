package cn.asany.shanhai.core.service;

import cn.asany.shanhai.core.bean.ModelFeature;
import cn.asany.shanhai.core.dao.ModelFeatureDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ModelFeatureService {
    @Autowired
    private ModelFeatureDao modelFeatureDao;

    public void save(ModelFeature feature) {
        modelFeatureDao.save(feature);
    }

    public void clear() {
        modelFeatureDao.deleteAllInBatch();
    }
}
