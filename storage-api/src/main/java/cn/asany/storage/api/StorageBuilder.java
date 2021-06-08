package cn.asany.storage.api;


/**
 * @author limaofeng
 */
public interface StorageBuilder<T extends Storage, C extends IStorageConfig> {

    public boolean supports(Class<C> config);

    T build(C config);

}
