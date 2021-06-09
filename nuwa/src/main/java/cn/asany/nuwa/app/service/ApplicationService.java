package cn.asany.nuwa.app.service;

import cn.asany.nuwa.app.bean.Application;
import cn.asany.nuwa.app.bean.ClientSecret;
import cn.asany.nuwa.app.dao.ApplicationDao;
import cn.asany.nuwa.app.service.dto.NativeApplication;
import cn.asany.nuwa.app.service.dto.OAuthApplication;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.security.oauth2.core.ClientDetails;
import org.jfantasy.framework.security.oauth2.core.ClientDetailsService;
import org.jfantasy.framework.security.oauth2.core.ClientRegistrationException;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 应用服务
 *
 * @author limaofeng
 */
@Service
public class ApplicationService implements ClientDetailsService {

    private static final String NONCE_CHARS = "abcdef0123456789";

    @Autowired
    private ApplicationDao applicationDao;

    public List<Application> findAll(List<PropertyFilter> filter) {
        return applicationDao.findAll(filter);
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        Optional<Application> optional = this.applicationDao.findOne(PropertyFilter.builder().equal("clientId", clientId).equal("enabled", true).build());
        if (!optional.isPresent()) {
            throw new ClientRegistrationException("[client_id=" + clientId + "]不存在");
        }
        return optional.get();
    }

    public Application createApplication(OAuthApplication app) {
        String clientId = StringUtil.generateNonceString(NONCE_CHARS, 20);
        String clientSecret = StringUtil.generateNonceString(NONCE_CHARS, 40);
        List<ClientSecret> clientSecrets = new ArrayList<>();
        clientSecrets.add(ClientSecret.builder().id(1L).client(clientId).secret(clientSecret).build());
        return Application.builder().clientId(clientId).clientSecretsAlias(clientSecrets).build();
    }

    public Application createApplication(NativeApplication app) {
        String clientId = StringUtil.generateNonceString(NONCE_CHARS, 20);
        String clientSecret = StringUtil.generateNonceString(NONCE_CHARS, 40);
        List<ClientSecret> clientSecrets = new ArrayList<>();
        clientSecrets.add(ClientSecret.builder().id(1L).client(clientId).secret(clientSecret).build());
        return Application.builder().clientId(clientId).clientSecretsAlias(clientSecrets).build();
    }
}
