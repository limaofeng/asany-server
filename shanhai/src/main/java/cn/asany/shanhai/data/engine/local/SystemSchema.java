package cn.asany.shanhai.data.engine.local;

import cn.asany.shanhai.data.domain.DataSetConfig;
import cn.asany.shanhai.data.engine.DataSet;
import cn.asany.shanhai.data.engine.Field;
import cn.asany.shanhai.data.engine.ISchema;

public class SystemSchema implements ISchema<Object, Object> {
  @Override
  public String getId() {
    return null;
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public Field[] getFields() {
    return new Field[0];
  }

  @Override
  public Field getField(String key) {
    return null;
  }

  @Override
  public Field getPrimaryKey() {
    return null;
  }

  @Override
  public DataSet<Object> load(DataSetConfig config) {
    return null;
  }

  @Override
  public DataSet<Object> load(DataSetConfig config, int offset, int limit) {
    return null;
  }

  @Override
  public Object get(Object id) {
    return null;
  }

  @Override
  public Object insert(Object object) {
    return null;
  }

  @Override
  public void update(Object object) {}

  @Override
  public void delete(Object object) {}
}
