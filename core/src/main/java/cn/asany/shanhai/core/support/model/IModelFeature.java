package cn.asany.shanhai.core.support.model;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.ModelEndpoint;
import cn.asany.shanhai.core.bean.ModelField;
import cn.asany.shanhai.core.support.model.features.*;

import java.util.Collections;
import java.util.List;

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

    default List<Model> getTypes(Model model) {
        return Collections.emptyList();
    }

    default List<ModelEndpoint> getEndpoints(Model model) {
        return Collections.emptyList();
    }
}
