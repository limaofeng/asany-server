package cn.asany.autoconfigure;

import cn.asany.storage.core.DefaultStorageResolver;
import cn.asany.storage.core.IStorageConfig;
import cn.asany.storage.core.StorageBuilder;
import cn.asany.storage.core.StorageResolver;
import cn.asany.storage.core.engine.minio.MinIOStorageConfig;
import org.jfantasy.graphql.SchemaParserDictionaryBuilder;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author limaofeng
 */
@Configuration
@EntityScan("cn.asany.storage.data.bean")
@ComponentScan("cn.asany.storage.core.engine")
public class StorageAutoConfiguration {

    @Bean
    public StorageResolver storageResolver(List<StorageBuilder> builders) {
        return new DefaultStorageResolver(builders);
    }

    @Bean
    public SchemaParserDictionaryBuilder storageSchemaParserDictionaryBuilder (){
        return dictionary -> {
            dictionary.add("StorageProperties", IStorageConfig.class);
            dictionary.add("MinIOProperties", MinIOStorageConfig.class);
        };
    }

}
