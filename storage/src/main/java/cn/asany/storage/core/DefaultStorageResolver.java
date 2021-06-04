package cn.asany.storage.core;

import org.apache.commons.collections.map.HashedMap;
import org.jfantasy.framework.jackson.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultStorageResolver implements StorageResolver {

    private Map<IStorageConfig, Storage> storages = new HashedMap();
    private List<StorageBuilder> builders = new ArrayList<>();

    public DefaultStorageResolver(List<StorageBuilder> builders) {
        this.builders = builders;
    }

    @Override
    public Storage resolve(String id) {
        return null;
    }

    @Override
    public Storage resolve(IStorageConfig config) {
        if (storages.containsKey(config)) {
            return storages.get(config);
        }
        for (StorageBuilder builder : builders) {
            if (builder.supports(config.getClass())) {
                Storage storage = builder.build(config);
                if (storage != null) {
                    storages.put(config, storage);
                    return storage;
                }
            }
        }
        throw new FileStoreException("不能创建 IStorage For " + JSON.serialize(config));
    }


}
