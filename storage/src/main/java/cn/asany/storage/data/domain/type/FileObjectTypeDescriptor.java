package cn.asany.storage.data.domain.type;

import cn.asany.storage.api.FileObject;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.MutableMutabilityPlan;
import org.jfantasy.framework.jackson.JSON;

public class FileObjectTypeDescriptor extends AbstractTypeDescriptor<FileObject> {

  public static final FileObjectTypeDescriptor INSTANCE = new FileObjectTypeDescriptor();

  public FileObjectTypeDescriptor() {
    super(
        FileObject.class,
        new MutableMutabilityPlan<FileObject>() {
          @Override
          protected FileObject deepCopyNotNull(FileObject value) {
            return value;
          }
        });
  }

  @Override
  public String toString(FileObject value) {
    return value.toString();
  }

  @Override
  public FileObject fromString(String value) {
    return JSON.deserialize(value, FileObject.class);
  }

  @Override
  public <X> X unwrap(FileObject value, Class<X> type, WrapperOptions options) {
    //noinspection unchecked
    return (X) toString(value);
  }

  @Override
  public <X> FileObject wrap(X value, WrapperOptions options) {
    return fromString((String) value);
  }
}
