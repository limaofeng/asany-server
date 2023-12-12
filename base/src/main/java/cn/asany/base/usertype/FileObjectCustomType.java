package cn.asany.base.usertype;

import cn.asany.storage.api.FileObject;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.jdbc.JsonJdbcType;

public class FileObjectCustomType extends AbstractSingleColumnStandardBasicType<FileObject> {

  public FileObjectCustomType() {
    super(JsonJdbcType.INSTANCE, FileObjectTypeDescriptor.INSTANCE);
  }

  @Override
  public String getName() {
    return "file";
  }
}
