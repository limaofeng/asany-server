package cn.asany.ui.library;

import java.util.List;

public interface OplogDataCollector {

  OplogDataCollector EMPTY_OPLOG_DATA_COLLECTOR = new OplogDataCollector() {};

  default Class getEntityClass() {
    return null;
  }

  default String getEntityName() {
    return null;
  }

  default String getTableName() {
    return null;
  }

  default String getPrimarykeyName() {
    return null;
  }

  default Object getPrimarykey() {
    return null;
  }

  default List<String> getOwners() {
    return null;
  }
}
