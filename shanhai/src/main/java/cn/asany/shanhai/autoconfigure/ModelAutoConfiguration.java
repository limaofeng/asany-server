package cn.asany.shanhai.autoconfigure;

import cn.asany.shanhai.core.support.ModelParser;
import cn.asany.shanhai.core.support.graphql.DynamicGraphQLSchemaProvider;
import cn.asany.shanhai.core.support.graphql.GraphQLReloadSchemaProvider;
import cn.asany.shanhai.core.support.graphql.ModelEndpointDataFetcherFactory;
import cn.asany.shanhai.core.support.tools.DynamicClassGenerator;
import graphql.kickstart.autoconfigure.tools.SchemaDirective;
import graphql.kickstart.autoconfigure.tools.SchemaStringProvider;
import graphql.kickstart.tools.*;
import graphql.kickstart.tools.proxy.ProxyHandler;
import graphql.schema.GraphQLScalarType;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.visibility.GraphqlFieldVisibility;
import java.util.List;
import java.util.Optional;
import org.jfantasy.graphql.SchemaParserDictionaryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Model Auto Configuration
 *
 * @author limaofeng
 */
@Configuration
public class ModelAutoConfiguration {

  @Bean(initMethod = "init")
  public ModelParser modelParser(
      List<GraphQLResolver<?>> resolvers,
      SchemaStringProvider schemaStringProvider,
      SchemaParserOptions.Builder optionsBuilder,
      @Autowired(required = false) SchemaParserDictionary dictionary,
      @Autowired(required = false) GraphQLScalarType[] scalars,
      @Autowired(required = false) List<SchemaDirective> directives,
      @Autowired(required = false) List<SchemaDirectiveWiring> directiveWirings) {
    return new ModelParser(
        resolvers,
        schemaStringProvider,
        optionsBuilder,
        dictionary,
        scalars,
        directives,
        directiveWirings);
  }

  @Bean
  public ModelEndpointDataFetcherFactory modelEndpointDataFetcherFactory(ModelParser modelParser) {
    return new ModelEndpointDataFetcherFactory(modelParser);
  }

  @Bean
  public DynamicClassGenerator dynamicClassGenerator() {
    return new DynamicClassGenerator();
  }

  @Bean
  @ConfigurationProperties("graphql.tools.schema-parser-options")
  public SchemaParserOptions.Builder customOptionsBuilder(
      @Autowired(required = false) PerFieldObjectMapperProvider perFieldObjectMapperProvider,
      @Autowired(required = false) List<SchemaParserOptions.GenericWrapper> genericWrappers,
      @Autowired(required = false) ObjectMapperConfigurer objectMapperConfigurer,
      @Autowired(required = false) List<ProxyHandler> proxyHandlers,
      @Autowired(required = false) CoroutineContextProvider coroutineContextProvider,
      @Autowired(required = false) List<TypeDefinitionFactory> typeDefinitionFactories,
      @Autowired(required = false) GraphqlFieldVisibility fieldVisibility) {
    SchemaParserOptions.Builder optionsBuilder = SchemaParserOptions.newOptions();

    if (perFieldObjectMapperProvider != null) {
      optionsBuilder.objectMapperProvider(perFieldObjectMapperProvider);
    } else {
      optionsBuilder.objectMapperConfigurer(objectMapperConfigurer);
    }

    Optional.ofNullable(genericWrappers).ifPresent(optionsBuilder::genericWrappers);

    if (proxyHandlers != null) {
      proxyHandlers.forEach(optionsBuilder::addProxyHandler);
    }

    Optional.ofNullable(coroutineContextProvider)
        .ifPresent(optionsBuilder::coroutineContextProvider);

    if (typeDefinitionFactories != null) {
      typeDefinitionFactories.forEach(optionsBuilder::typeDefinitionFactory);
    }

    Optional.ofNullable(fieldVisibility).ifPresent(optionsBuilder::fieldVisibility);

    //    optionsBuilder.allowUnimplementedResolvers(true);
    //    optionsBuilder.includeUnusedTypes(true);
    //    optionsBuilder.missingResolverDataFetcher(new CustomMissingResolverDataFetcher());
    //    optionsBuilder.preferGraphQLResolver(true);

    return optionsBuilder;
  }

  @Bean
  public SchemaParser schemaParser(ModelParser modelParser) {
    return modelParser.getSchemaParser();
  }

  @Bean
  public GraphQLReloadSchemaProvider graphqlSchemaProvider(ModelParser modelParser) {
    return new DynamicGraphQLSchemaProvider(modelParser);
  }

  @Bean("MissingTypeDefinition.SchemaParserDictionaryBuilder")
  public SchemaParserDictionaryBuilder missingSchemaDictionary() {
    return dictionary -> {
      //      dictionary.add("Collection", AsmUtil.makeClass("cn.asany.shanhai.gen.Collection"));
      //      dictionary.add("MailboxAddress",
      // AsmUtil.makeClass("cn.asany.shanhai.gen.MailboxAddress"));
      //      dictionary.add(
      //          "ApplicationVariable",
      // AsmUtil.makeClass("cn.asany.shanhai.gen.ApplicationVariable"));
      //      dictionary.add("Meta", AsmUtil.makeClass("cn.asany.shanhai.gen.Meta"));
      //      dictionary.add("GraphQLEndpoint",
      // AsmUtil.makeClass("cn.asany.shanhai.gen.GraphQLEndpoint"));
      //      dictionary.add(
      //          "OrganiztionEmployee",
      // AsmUtil.makeClass("cn.asany.shanhai.gen.OrganiztionEmployee"));
      //      dictionary.add(
      //          "EmployeeConnection",
      // AsmUtil.makeClass("cn.asany.shanhai.gen.EmployeeConnection"));
      //      dictionary.add(
      //          "ExcelEmployeeConnection",
      //          AsmUtil.makeClass("cn.asany.shanhai.gen.ExcelEmployeeConnection"));
      //      dictionary.add(
      //          "ExcelEmployeeEdge", AsmUtil.makeClass("cn.asany.shanhai.gen.ExcelEmployeeEdge"));
      //      dictionary.add("EmployeeEdge",
      // AsmUtil.makeClass("cn.asany.shanhai.gen.EmployeeEdge"));
      //      dictionary.add("ExcelEmployee",
      // AsmUtil.makeClass("cn.asany.shanhai.gen.ExcelEmployee"));
      //      dictionary.add("ResourceType",
      // AsmUtil.makeClass("cn.asany.shanhai.gen.ResourceType"));
      //      dictionary.add("UserConnection",
      // AsmUtil.makeClass("cn.asany.shanhai.gen.UserConnection"));
      //      dictionary.add("UserEdge", AsmUtil.makeClass("cn.asany.shanhai.gen.UserEdge"));
      //      dictionary.add(
      //          "RouteComponentWrapper",
      // AsmUtil.makeClass("cn.asany.shanhai.gen.RouteComponentWrapper"));
    };
  }
}
