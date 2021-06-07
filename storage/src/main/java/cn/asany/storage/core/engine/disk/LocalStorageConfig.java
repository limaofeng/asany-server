package cn.asany.storage.core.engine.disk;

import cn.asany.storage.core.AbstractStorageConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author limaofeng
 */
@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class LocalStorageConfig extends AbstractStorageConfig {

    private String defaultDir;

}
