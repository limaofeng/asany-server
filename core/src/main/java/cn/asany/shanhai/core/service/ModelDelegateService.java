package cn.asany.shanhai.core.service;

import cn.asany.shanhai.core.bean.ModelDelegate;
import cn.asany.shanhai.core.dao.ModelEndpointDao;
import cn.asany.shanhai.core.dao.ModelDelegateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Transient;
import java.util.Optional;

@Service
@Transactional
public class ModelDelegateService {

    @Autowired
    private ModelEndpointDao modelEndpointDao;
    @Autowired
    private ModelDelegateDao modelDelegateDao;

    public ModelDelegate save(ModelDelegate delegate) {
        Optional<ModelDelegate> optional = modelDelegateDao.findOne(Example.of(delegate));
        return optional.orElseGet(() -> modelDelegateDao.save(delegate));
    }
}
