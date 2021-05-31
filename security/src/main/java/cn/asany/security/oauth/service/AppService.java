package cn.asany.security.oauth.service;

import cn.asany.security.oauth.bean.Application;
import cn.asany.security.oauth.dao.ApplicationDao;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class AppService {

    private final ApplicationDao applicationDao;

    @Autowired
    public AppService(ApplicationDao applicationDao) {
        this.applicationDao = applicationDao;
    }

    @Transactional
    public Application save(Application application) {
        return this.applicationDao.save(application);
    }

    @Transactional
    public Pager<Application> findPager(Pager<Application> pager, List<PropertyFilter> filters) {
        return this.applicationDao.findPager(pager, filters);
    }

//    @Transactional
//    public ApiKey save(String id, ApiKey apiKey) {
//        apiKey.setApplication(new Application());
////        apiKey.getApplication().setId(id);
//        return this.apiKeyDao.save(apiKey);
//    }

//    @Transactional
//    public Optional<ApiKey> get(String apiKey) {
//        return this.apiKeyDao.findById(apiKey);
//    }
//
//    @Transactional
//    public List<ApiKey> find(Long appid) {
//        return this.apiKeyDao.findAll(PropertyFilter.builder().equal("application.id", appid).build());
//    }

    public List<Application> findAll(List<PropertyFilter> filters) {
        return this.applicationDao.findAll(filters);
    }

    public Optional<Application> getApplication(String id) {
        return this.applicationDao.findById(id);
    }

    public Optional<Application> getApplicationByClient(String id) {
        return this.applicationDao.findOne(PropertyFilter.builder().equal("clientId", id).build());
    }
}