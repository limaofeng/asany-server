package cn.asany.storage.data.graphql;

import cn.asany.storage.data.bean.StorageConfig;
import cn.asany.storage.data.service.StorageService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author limaofeng
 */
@Component
public class StorageGraphQLGraphQLQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private StorageService storageService;

    public List<StorageConfig> storages() {
        return this.storageService.findAll();
    }

}
