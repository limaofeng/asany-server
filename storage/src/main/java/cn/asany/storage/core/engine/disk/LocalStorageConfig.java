package cn.asany.storage.core.engine.disk;

import cn.asany.storage.core.AbstractStorageConfig;
import lombok.*;

/** @author limaofeng */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LocalStorageConfig extends AbstractStorageConfig {

  private String defaultDir;
}
