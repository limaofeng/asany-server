package cn.asany.autoconfigure;

import cn.asany.storage.api.IStorageConfig;
import cn.asany.storage.api.StorageBuilder;
import cn.asany.storage.core.DefaultStorageResolver;
import cn.asany.storage.core.StorageResolver;
import cn.asany.storage.core.engine.minio.MinIOStorageConfig;
import cn.asany.storage.data.graphql.scalar.FileObjectCoercing;
import cn.asany.storage.data.service.FileService;
import graphql.kickstart.servlet.apollo.ApolloScalars;
import graphql.schema.GraphQLScalarType;
import org.jfantasy.graphql.SchemaParserDictionaryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
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
@ComponentScan("cn.asany.storage.core")
public class StorageAutoConfiguration {

    @Bean
    public GraphQLScalarType uploadScalarDefine() {
        return ApolloScalars.Upload;
    }

    @Bean
    public GraphQLScalarType fileByScalar() {
        return GraphQLScalarType.newScalar().name("FileObject").description("文件对象").coercing(new FileObjectCoercing()).build();
    }

    @Bean
    public StorageResolver storageResolver(List<StorageBuilder> builders) {
        return new DefaultStorageResolver(builders);
    }

    @Bean
    public SchemaParserDictionaryBuilder storageSchemaParserDictionaryBuilder() {
        return dictionary -> {
            dictionary.add("StorageProperties", IStorageConfig.class);
            dictionary.add("MinIOProperties", MinIOStorageConfig.class);
        };
    }

}
