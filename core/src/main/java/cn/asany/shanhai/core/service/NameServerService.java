package cn.asany.shanhai.core.service;

import cn.asany.shanhai.core.dao.NameServerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author limaofeng
 */
@Service
public class NameServerService {

    @Autowired
    private NameServerDao nameServerDao;

}
