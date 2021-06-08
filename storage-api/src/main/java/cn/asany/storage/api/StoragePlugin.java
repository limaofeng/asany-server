package cn.asany.storage.api;

public interface StoragePlugin {

    boolean supports(UploadContext context);

    FileObject upload(UploadContext context);

}
