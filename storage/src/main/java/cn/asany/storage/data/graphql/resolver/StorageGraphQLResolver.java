package cn.asany.storage.data.graphql.resolver;

import cn.asany.storage.data.bean.StorageConfig;
import cn.asany.storage.data.service.StorageService;
import graphql.kickstart.tools.GraphQLResolver;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

/**
 * 文件管理器
 *
 * @author limaofeng
 */
@Component
public class StorageGraphQLResolver implements GraphQLResolver<StorageConfig> {

  private final StorageService storageService;

  public StorageGraphQLResolver(StorageService storageService) {
    this.storageService = storageService;
  }

  public long totalFiles(StorageConfig config) {
    return this.storageService.totalFiles(config.getId());
  }

  public long usage(StorageConfig config) {
    long usage = this.storageService.usage(config.getId());
    BigDecimal bd = BigDecimal.valueOf(usage).divide(BigDecimal.valueOf(1024), RoundingMode.UP);
    return bd.setScale(0, RoundingMode.UP).longValue();
  }
}
