package cn.asany.storage.data.domain.type;

import cn.asany.storage.api.FileObject;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

public class FileObjectCustomType extends AbstractSingleColumnStandardBasicType<FileObject> {

  public FileObjectCustomType() {
    super(VarcharTypeDescriptor.INSTANCE, FileObjectTypeDescriptor.INSTANCE);
  }

  @Override
  public String getName() {
    return "file";
  }
}
