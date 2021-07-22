package cn.asany.ui.library.service;

import cn.asany.ui.library.bean.Oplog;
import cn.asany.ui.library.dao.OplogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OplogService {

    @Autowired
    private OplogDao oplogDao;

    public Oplog log() {
        return this.oplogDao.save(Oplog.builder().build());
    }

}
