package cn.asany.base.usertype;

import cn.asany.storage.api.FileObject;
import net.asany.jfantasy.framework.jackson.JSON;
import net.asany.jfantasy.framework.util.common.StringUtil;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractJavaType;
import org.hibernate.type.descriptor.java.MutableMutabilityPlan;

public class FileObjectTypeDescriptor extends AbstractJavaType<FileObject> {

  public static final FileObjectTypeDescriptor INSTANCE = new FileObjectTypeDescriptor();

  public FileObjectTypeDescriptor() {
    super(
        FileObject.class,
        new MutableMutabilityPlan<>() {
          @Override
          protected FileObject deepCopyNotNull(FileObject value) {
            return value;
          }
        });
  }

  @Override
  public String toString(FileObject value) {
    return JSON.serialize(value);
  }

  public FileObject fromString(String value) {
    if (StringUtil.isBlank(value)) {
      return null;
    }
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
