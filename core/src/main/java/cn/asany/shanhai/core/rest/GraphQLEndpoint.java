package cn.asany.shanhai.core.rest;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

@RestController
@Slf4j
public class GraphQLEndpoint {

    @PostMapping("/ext/graphql")
    public Map<String, Object> graphql(@RequestBody String body) {
        System.out.println(">>>>>>>>" + body);
        log.debug(body);
        ReadContext context = JsonPath.parse(body);
        body = JSON.getObjectMapper().convertValue(context.read("$.query"), String.class);
        String schema = FileUtil.readFile(GraphQLEndpoint.class.getResource("/test.xgraphqls").getFile());

        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);

        RuntimeWiring runtimeWiring = newRuntimeWiring()
            .type("Query", builder -> {
                builder.dataFetcher("hello", new StaticDataFetcher("world"));

                DataFetcher productsDataFetcher = new DataFetcher<List<Object>>() {
                    @Override
                    public List<Object> get(DataFetchingEnvironment environment) {
//                        DatabaseSecurityCtx ctx = environment.getContext();

//                        List<ProductDTO> products;
//                        String match = environment.getArgument("match");
//                        if (match != null) {
//                            products = fetchProductsFromDatabaseWithMatching(ctx, match);
//                        } else {
//                            products = fetchAllProductsFromDatabase(ctx);
//                        }
                        List data = new ArrayList<>();
                        Map<String, Object> x = new HashMap<>();
                        Map<String, Object> user = new HashMap<>();
                        user.put("id", "0");
//                        user.put("name", "limaofeng");
                        x.put("id", "1");
                        x.put("name", "2");
                        x.put("user", user);
                        data.add(x);
                        return data;
                    }
                };

                builder.dataFetcher("products", productsDataFetcher);
                return builder;
            })
            .type("User", builder -> {
                builder.dataFetcher("name", new StaticDataFetcher("王子"));
                return builder;
            })
            .build();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);

        GraphQL build = GraphQL.newGraphQL(graphQLSchema).build();
        ExecutionResult executionResult = build.execute(body);

//        System.out.println(executionResult.getData().toString());
        Map<String, Object> result = new HashMap<>();
        if (!executionResult.getErrors().isEmpty()) {
            result.put("errors", executionResult.getErrors());
        }
        result.put("data", executionResult.getData());
        return result;
    }

}
