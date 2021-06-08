package cn.asany.storage.core.engine.disk;


import cn.asany.storage.core.FileStoreException;
import cn.asany.storage.api.StorageBuilder;
import org.springframework.stereotype.Component;

@Component
public class LocalStorageBuilder implements StorageBuilder<LocalStorage, LocalStorageConfig> {

    @Override
    public boolean supports(Class<LocalStorageConfig> config) {
        return LocalStorageConfig.class.isAssignableFrom(config);
    }

    @Override
    public LocalStorage build(LocalStorageConfig config) {
        String defaultDir = config.getDefaultDir();
        if (defaultDir == null) {
            throw new FileStoreException(" LocalFileManager 未配置 defaultDir 项");
        }
        return new LocalStorage(defaultDir);
    }
}
