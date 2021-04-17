package cn.asany.shanhai.gateway.service;

import cn.asany.shanhai.gateway.bean.ModelGroup;
import cn.asany.shanhai.gateway.dao.ModelGroupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ModelGroupService {

    @Autowired
    private ModelGroupDao modelGroupDao;

    public List<ModelGroup> groups() {
        return this.modelGroupDao.findAllWithItems();
    }

}
