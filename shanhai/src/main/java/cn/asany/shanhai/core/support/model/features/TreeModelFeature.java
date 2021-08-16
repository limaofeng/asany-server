package cn.asany.shanhai.core.support.model.features;

import cn.asany.shanhai.core.support.model.IModelFeature;
import lombok.Data;

@Data
public class TreeModelFeature implements IModelFeature {
  public static final String ID = "tree";
  private String id = ID;
}
