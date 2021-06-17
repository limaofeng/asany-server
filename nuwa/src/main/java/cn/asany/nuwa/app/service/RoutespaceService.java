package cn.asany.nuwa.app.service;

import cn.asany.nuwa.app.bean.Routespace;
import cn.asany.nuwa.app.dao.RoutespaceDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoutespaceService {

    private final RoutespaceDao routespaceDao;

    public RoutespaceService(RoutespaceDao routespaceDao) {
        this.routespaceDao = routespaceDao;
    }

    public List<Routespace> findAll() {
        return routespaceDao.findAll();
    }

    public Routespace createRoutespace(Routespace routespace) {
        return this.routespaceDao.save(routespace);
    }

}
