package cn.asany.shanhai.core.support.model.features;

import cn.asany.shanhai.core.support.model.IModelFeature;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class SlaveModelFeature implements IModelFeature {
  public static final String ID = "slave";
  private String id = ID;
}
