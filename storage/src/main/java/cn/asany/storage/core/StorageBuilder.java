package cn.asany.storage.core;


/**
 * @author limaofeng
 */
public interface StorageBuilder<T extends Storage, C extends IStorageConfig> {

    public boolean supports(Class<C> config);

    T build(C config);

}
