package cn.asany.storage.core.engine.disk;

import cn.asany.storage.core.AbstractStorageConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author limaofeng
 */
@Data
@Builder
@AllArgsConstructor
public class LocalStorageConfig extends AbstractStorageConfig {

    private String defaultDir;

}
