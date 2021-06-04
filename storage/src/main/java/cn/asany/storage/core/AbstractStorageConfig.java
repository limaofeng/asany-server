package cn.asany.storage.core;


import lombok.Data;

@Data
public abstract class AbstractStorageConfig implements IStorageConfig {

    protected String id;

    @Override
    public String id() {
        return id;
    }
}
