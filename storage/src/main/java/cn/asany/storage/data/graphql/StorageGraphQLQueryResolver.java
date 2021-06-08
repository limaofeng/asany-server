package cn.asany.storage.data.graphql;

import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.bean.StorageConfig;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.data.service.StorageService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author limaofeng
 */
@Component
public class StorageGraphQLQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private StorageService storageService;
    @Autowired
    private FileService fileService;

    public List<StorageConfig> storages() {
        return this.storageService.findAll();
    }

    public Optional<FileDetail> file(Long id) {
        return fileService.findById(id);
    }

}
