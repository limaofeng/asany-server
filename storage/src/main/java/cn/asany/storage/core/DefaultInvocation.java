package cn.asany.storage.core;

import cn.asany.storage.api.*;
import java.util.Iterator;

public class DefaultInvocation implements Invocation {

  private final StoragePlugin handler;
  private final UploadContext context;
  private final Iterator<StoragePlugin> iterator;

  public DefaultInvocation(UploadContext context, Iterator<StoragePlugin> iterator) {
    this.context = context;
    this.iterator = iterator;
    this.handler = iterator.hasNext() ? iterator.next() : null;
  }

  @Override
  public FileObject invoke() throws UploadException {
    if (this.handler == null) {
      throw new UploadException("handler is null");
    }
    return this.handler.upload(this.context, new DefaultInvocation(this.context, this.iterator));
  }

  public UploadContext getContext() {
    return context;
  }
}
