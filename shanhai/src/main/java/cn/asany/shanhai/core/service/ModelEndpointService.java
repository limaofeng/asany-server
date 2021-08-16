package cn.asany.shanhai.core.service;

import cn.asany.shanhai.core.dao.ModelDelegateDao;
import cn.asany.shanhai.core.dao.ModelEndpointDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModelEndpointService {

  @Autowired private ModelEndpointDao modelEndpointDao;
  @Autowired private ModelDelegateDao modelDelegateDao;
}
