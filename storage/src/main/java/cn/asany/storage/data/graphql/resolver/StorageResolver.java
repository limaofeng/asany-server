package cn.asany.storage.data.graphql.resolver;

import cn.asany.storage.data.bean.StorageConfig;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

@Component
public class StorageResolver implements GraphQLResolver<StorageConfig> {}
