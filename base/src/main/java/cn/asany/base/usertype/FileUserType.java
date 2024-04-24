package cn.asany.base.usertype;

import cn.asany.storage.api.FileObject;
import java.util.function.BiConsumer;
import org.hibernate.type.descriptor.java.BasicJavaType;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.hibernate.type.descriptor.jdbc.JsonJdbcType;
import org.hibernate.usertype.BaseUserTypeSupport;

public class FileUserType extends BaseUserTypeSupport<FileObject> {
  @Override
  protected void resolve(BiConsumer<BasicJavaType<FileObject>, JdbcType> resolutionConsumer) {
    resolutionConsumer.accept(FileObjectTypeDescriptor.INSTANCE, JsonJdbcType.INSTANCE);
  }
}
