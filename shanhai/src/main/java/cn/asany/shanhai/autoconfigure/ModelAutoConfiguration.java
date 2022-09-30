package cn.asany.shanhai.autoconfigure;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.domain.ModelRelation;
import cn.asany.shanhai.core.support.ModelParser;
import cn.asany.shanhai.core.support.graphql.ModelEndpointDataFetcherFactory;
import cn.asany.shanhai.core.support.graphql.config.CustomTypeDefinitionFactory;
import org.jfantasy.framework.util.asm.AsmUtil;
import org.jfantasy.graphql.SchemaParserDictionaryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelAutoConfiguration {

  @Bean(initMethod = "init")
  public ModelParser modelParser() {
    return new ModelParser();
  }

  @Bean
  public ModelEndpointDataFetcherFactory modelEndpointDataFetcherFactory(ModelParser modelParser) {
    return new ModelEndpointDataFetcherFactory(modelParser);
  }

  @Bean("CustomTypeDefinition.SchemaParserDictionaryBuilder")
  public SchemaParserDictionaryBuilder customSchemaDictionary(ModelParser modelParser) {
    return dictionary -> {
      for (Model model : modelParser.getModels()) {
        String classname = model.getModule().getCode().concat(".").concat(model.getCode());
        dictionary.add(model.getCode(), AsmUtil.makeClass(classname));

        for (ModelRelation relation : model.getRelations()) {
          Model type = relation.getInverse();

        }
      }
    };
  }

  @Bean
  public CustomTypeDefinitionFactory customTypeDefinitionFactory(ModelParser modelParser) {
    return new CustomTypeDefinitionFactory(modelParser);
  }

  @Bean("MissingTypeDefinition.SchemaParserDictionaryBuilder")
  public SchemaParserDictionaryBuilder missingSchemaDictionary() {
    return dictionary -> {
      dictionary.add("Collection", AsmUtil.makeClass("cn.asany.shanhai.gen.Collection"));
      dictionary.add("MailboxAddress", AsmUtil.makeClass("cn.asany.shanhai.gen.MailboxAddress"));
      dictionary.add(
          "ApplicationVariable", AsmUtil.makeClass("cn.asany.shanhai.gen.ApplicationVariable"));
      dictionary.add("Meta", AsmUtil.makeClass("cn.asany.shanhai.gen.Meta"));
      dictionary.add("GraphQLEndpoint", AsmUtil.makeClass("cn.asany.shanhai.gen.GraphQLEndpoint"));
      dictionary.add(
          "OrganiztionEmployee", AsmUtil.makeClass("cn.asany.shanhai.gen.OrganiztionEmployee"));
      dictionary.add(
          "EmployeeConnection", AsmUtil.makeClass("cn.asany.shanhai.gen.EmployeeConnection"));
      dictionary.add(
          "ExcelEmployeeConnection",
          AsmUtil.makeClass("cn.asany.shanhai.gen.ExcelEmployeeConnection"));
      dictionary.add(
          "ExcelEmployeeEdge", AsmUtil.makeClass("cn.asany.shanhai.gen.ExcelEmployeeEdge"));
      dictionary.add("EmployeeEdge", AsmUtil.makeClass("cn.asany.shanhai.gen.EmployeeEdge"));
      dictionary.add("ExcelEmployee", AsmUtil.makeClass("cn.asany.shanhai.gen.ExcelEmployee"));
      dictionary.add("ResourceType", AsmUtil.makeClass("cn.asany.shanhai.gen.ResourceType"));
      dictionary.add("UserConnection", AsmUtil.makeClass("cn.asany.shanhai.gen.UserConnection"));
      dictionary.add("UserEdge", AsmUtil.makeClass("cn.asany.shanhai.gen.UserEdge"));
      dictionary.add(
          "RouteComponentWrapper", AsmUtil.makeClass("cn.asany.shanhai.gen.RouteComponentWrapper"));
    };
  }
}
