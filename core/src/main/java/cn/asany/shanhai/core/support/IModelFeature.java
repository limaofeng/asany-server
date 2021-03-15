package cn.asany.shanhai.core.support;

import cn.asany.shanhai.core.bean.ModelField;

import java.util.Collections;
import java.util.List;

public interface IModelFeature {

    default List<ModelField> fields() {
        return Collections.emptyList();
    }

    String getId();
}
