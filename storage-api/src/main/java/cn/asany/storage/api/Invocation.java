package cn.asany.storage.api;

public interface Invocation {

  UploadContext getContext();

  FileObject invoke() throws UploadException;
}
