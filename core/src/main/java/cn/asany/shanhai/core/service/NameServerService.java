package cn.asany.shanhai.core.service;

import cn.asany.shanhai.core.bean.Service;
import cn.asany.shanhai.core.dao.NameServerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author limaofeng
 */
@org.springframework.stereotype.Service
@Transactional
public class NameServerService {

    @Autowired
    private NameServerDao nameServerDao;

    public void save(Service service) {
        this.nameServerDao.save(service);
    }

}
