package cn.asany.security.oauth.service;

import cn.asany.security.oauth.bean.OAuthApplication;
import cn.asany.security.oauth.dao.OAuthApplicationDao;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.security.oauth2.core.ClientDetails;
import org.jfantasy.framework.security.oauth2.core.ClientDetailsService;
import org.jfantasy.framework.security.oauth2.core.ClientRegistrationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class OAuthApplicationService implements ClientDetailsService {

    private final OAuthApplicationDao OAuthApplicationDao;

    @Autowired
    public OAuthApplicationService(OAuthApplicationDao OAuthApplicationDao) {
        this.OAuthApplicationDao = OAuthApplicationDao;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        Optional<OAuthApplication> optional = this.OAuthApplicationDao.findOne(PropertyFilter.builder().equal("clientId", clientId).equal("enabled", true).build());
        if (!optional.isPresent()) {
            throw new ClientRegistrationException("[client_id=" + clientId + "]不存在");
        }
        return optional.get();
    }

    @Transactional
    public OAuthApplication save(OAuthApplication OAuthApplication) {
        return this.OAuthApplicationDao.save(OAuthApplication);
    }

    @Transactional
    public Pager<OAuthApplication> findPager(Pager<OAuthApplication> pager, List<PropertyFilter> filters) {
        return this.OAuthApplicationDao.findPager(pager, filters);
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

    public List<OAuthApplication> findAll(List<PropertyFilter> filters) {
        return this.OAuthApplicationDao.findAll(filters);
    }

    public Optional<OAuthApplication> getApplication(String id) {
        return this.OAuthApplicationDao.findById(id);
    }

    public Optional<OAuthApplication> getApplicationByClient(String id) {
        return this.OAuthApplicationDao.findOne(PropertyFilter.builder().equal("clientId", id).build());
    }

}