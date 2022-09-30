package cn.asany.shanhai.core.support.model;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.domain.ModelEndpoint;
import cn.asany.shanhai.core.domain.ModelField;
import cn.asany.shanhai.core.domain.ModelRelation;
import cn.asany.shanhai.core.support.model.features.*;
import java.util.Collections;
import java.util.List;

/**
 * 实体特征
 *
 * @author limaofeng
 */
public interface IModelFeature {
  String LOGICAL_DELETION = LogicalDeletionFeature.ID;
  String SYSTEM_FIELDS = SystemFieldsFeature.ID;
  String MASTER_MODEL = MasterModelFeature.ID;
  String SLAVE_MODEL = SlaveModelFeature.ID;
  String TREE_MODEL = TreeModelFeature.ID;

  String getId();

  default List<ModelField> fields() {
    return Collections.emptyList();
  }

  default List<ModelRelation> getTypes(Model model) {
    return Collections.emptyList();
  }

  default List<ModelEndpoint> getEndpoints(Model model) {
    return Collections.emptyList();
  }
}
