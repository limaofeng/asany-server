package cn.asany.shanhai.gateway.util;

import static graphql.schema.idl.SchemaPrinter.Options.defaultOptions;

import cn.asany.shanhai.gateway.domain.ServiceSchemaVersionPatch;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.introspection.IntrospectionQuery;
import graphql.introspection.IntrospectionResultToSchema;
import graphql.language.Document;
import graphql.schema.idl.SchemaPrinter;
import java.util.*;
import lombok.SneakyThrows;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.client.GraphQLResponse;
import org.jfantasy.graphql.client.GraphQLTemplate;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.client.RestTemplate;

public class SchemaUtils {
  private static ResourceLoader resourceLoader;
  private static RestTemplate restTemplate;
  private static ObjectMapper objectMapper;

  public static GraphQLTemplate createGraphQLClient(String url) {
    return new GraphQLTemplate(resourceLoader, restTemplate, url, objectMapper);
  }

  public static GraphQLSchema loadSchema(String schema) {
    return GraphQLSchema.builder().schema(schema).build();
  }

  @SneakyThrows
  public static GraphQLSchema loadRemoteSchema(String url) {
    GraphQLTemplate graphQLTemplate = createGraphQLClient(url);

    GraphQLResponse response =
        graphQLTemplate.post(IntrospectionQuery.INTROSPECTION_QUERY, "IntrospectionQuery");

    Document schemaDefinition =
        new IntrospectionResultToSchema()
            .createSchemaDefinition(response.get("$.data", HashMap.class));

    SchemaPrinter.Options noDirectivesOption = defaultOptions().includeDirectives(false);

    SchemaPrinter schemaPrinter = new SchemaPrinter(noDirectivesOption);

    String result = schemaPrinter.print(schemaDefinition);

    return loadSchema(result);
  }

  public static List<ServiceSchemaVersionPatch> diff(
      GraphQLSchema prevSchema, GraphQLSchema graphQLSchema) {
    List<DiffObject> diffs = Equator.diff(prevSchema, graphQLSchema);

    DiffObject typeMap = ObjectUtil.find(diffs, "path", "/typeMap");
    DiffObject subscriptionType = ObjectUtil.find(diffs, "path", "/subscriptionType");
    DiffObject mutationType = ObjectUtil.find(diffs, "path", "/mutationType");
    DiffObject queryType = ObjectUtil.find(diffs, "path", "/queryType");

    List<ServiceSchemaVersionPatch> items = new ArrayList<>();

    if (typeMap != null) {

      List<DiffObject> allType = typeMap.getDiffObjects();
      if (subscriptionType != null) {
        allType.add(subscriptionType);
      }
      if (mutationType != null) {
        allType.add(mutationType);
      }
      if (queryType != null) {
        allType.add(queryType);
      }

      for (DiffObject typeDiffObject : allType) {
        if (typeDiffObject.getStatus() == DiffObject.DiffStatus.D) {
          GraphQLObjectType type = ((GraphQLObjectType) typeDiffObject.getPrev());
          items.add(
              ServiceSchemaVersionPatch.builder()
                  .name(type.getId())
                  .description(type.getDescription())
                  .status(DiffObject.DiffStatus.D)
                  .build());
          continue;
        }

        GraphQLObjectType type = ((GraphQLObjectType) typeDiffObject.getCurrent());

        ServiceSchemaVersionPatch.ServiceSchemaVersionPatchBuilder builder =
            ServiceSchemaVersionPatch.builder();
        builder
            .name(type.getId())
            .type(type.getType())
            .description(type.getDescription())
            .status(typeDiffObject.getStatus());
        items.add(builder.build());

        if (typeDiffObject.getStatus() == DiffObject.DiffStatus.A) {
          for (GraphQLField fieldDefinition : type.getFields()) {
            items.add(
                ServiceSchemaVersionPatch.builder()
                    .name(type.getId() + "." + fieldDefinition.getId())
                    .description(fieldDefinition.getDescription())
                    .status(DiffObject.DiffStatus.A)
                    .build());
          }
        } else if (typeDiffObject.getStatus() == DiffObject.DiffStatus.M) {
          DiffObject fieldMap =
              ObjectUtil.find(
                  typeDiffObject.getDiffObjects(), "path", typeDiffObject.getPath() + "/fieldMap");
          if (fieldMap == null) {
            continue;
          }
          for (DiffObject fieldDiffObject : fieldMap.getDiffObjects()) {
            GraphQLField fieldDefinition =
                fieldDiffObject.getStatus() == DiffObject.DiffStatus.D
                    ? ((GraphQLField) fieldDiffObject.getPrev())
                    : ((GraphQLField) fieldDiffObject.getCurrent());

            items.add(
                ServiceSchemaVersionPatch.builder()
                    .name(type.getId() + "." + fieldDefinition.getId())
                    .description(fieldDefinition.getDescription())
                    .status(fieldDiffObject.getStatus())
                    .build());
          }
        }

        //                if (typeDiffObject.getStatus() == DiffObject.DiffStatus.M) {
        //                    for (GraphQLFieldDefinition fieldDefinition : type.getFields()) {
        //                        items.add(GraphQLSchemaChangeItem.builder()
        //                            .id(type.getId() + "." + fieldDefinition.getId())
        //                            .description(fieldDefinition.getDescription())
        //                            .status("A")
        //                            .build());
        //                    }
        //                }

        //                if (typeDiffObject.getStatus() == DiffObject.DiffStatus.D) {
        //                    for (GraphQLFieldDefinition fieldDefinition : type.getFields()) {
        //                        items.add(GraphQLSchemaChangeItem.builder()
        //                            .id(type.getId() + "." + fieldDefinition.getId())
        //                            .description(fieldDefinition.getDescription())
        //                            .status("A")
        //                            .build());
        //                    }
        //                }

      }
    }

    for (ServiceSchemaVersionPatch item : items) {
      System.out.println(">>>>>>" + item);
    }

    return items;
  }

  public static List<DiffObject> parse(List<DiffObject> diffs, String[] names) {
    String type = names[0];
    String fieldName = names[1];
    if ("Mutation".equals(type)) {
      List<DiffObject> children = ObjectUtil.find(diffs, "path", "/mutationType").getDiffObjects();
      List<DiffObject> fieldMap =
          ObjectUtil.find(children, "path", "/mutationType/fieldMap").getDiffObjects();
      DiffObject field =
          ObjectUtil.find(fieldMap, "path", "/mutationType/fieldMap[" + fieldName + "]");
      return children;
    } else if ("Query".equals(type)) {
      return ObjectUtil.find(diffs, "path", "/queryType").getDiffObjects();
    } else {
      DiffObject typeMap = ObjectUtil.find(diffs, "path", "/typeMap");
      DiffObject typeDiffObject =
          ObjectUtil.find(typeMap.getDiffObjects(), "path", "/typeMap[" + type + "]");
      return typeDiffObject.getDiffObjects();
    }
  }

  private static void findField(List<DiffObject> diffs, String name) {}

  public static Set<DiffObject> findDifference(List<DiffObject> diffs, Set<String> keys) {
    Set<DiffObject> diffObjects = new LinkedHashSet<>();
    for (String name : keys) {
      if (name.contains(".")) {
        String[] names = name.split("\\.");
        diffObjects.addAll(
            ObjectUtil.filter(
                diffs,
                (item) ->
                    item.getPath()
                        .startsWith("/typeMap[" + names[0] + "]/fieldMap[" + names[1] + "]")));
      } else {
        diffObjects.addAll(
            ObjectUtil.filter(
                diffs, (item) -> item.getPath().startsWith("/typeMap[" + name + "]")));
      }
    }
    return diffObjects;
  }

  public static void setResourceLoader(ResourceLoader resourceLoader) {
    SchemaUtils.resourceLoader = resourceLoader;
  }

  public static void setRestTemplate(RestTemplate restTemplate) {
    SchemaUtils.restTemplate = restTemplate;
  }

  public static void setObjectMapper(ObjectMapper objectMapper) {
    SchemaUtils.objectMapper = objectMapper;
  }
}
