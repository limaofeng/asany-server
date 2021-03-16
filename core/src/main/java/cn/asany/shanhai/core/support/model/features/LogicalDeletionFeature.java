package cn.asany.shanhai.core.support.model.features;

import cn.asany.shanhai.core.support.model.IModelFeature;
import lombok.Data;

@Data
public class LogicalDeletionFeature implements IModelFeature {
    private String id = "LogicalDeletion";
}
