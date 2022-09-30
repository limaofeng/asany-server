package cn.asany.shanhai.core.support.graphql;

import static java.util.Objects.nonNull;

import graphql.kickstart.autoconfigure.tools.SchemaDirective;
import graphql.kickstart.autoconfigure.tools.SchemaStringProvider;
import graphql.kickstart.execution.config.GraphQLSchemaProvider;
import graphql.kickstart.servlet.config.GraphQLSchemaServletProvider;
import graphql.kickstart.tools.*;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaDirectiveWiring;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.HandshakeRequest;

public class DynamicGraphQLSchemaProvider implements GraphQLSchemaServletProvider {

  private GraphQLSchema graphQLSchema;
  private GraphQLSchema readOnlySchema;

  private final List<GraphQLResolver<?>> resolvers;
  private final SchemaStringProvider schemaStringProvider;
  private final SchemaParserOptions.Builder optionsBuilder;
  private final SchemaParserDictionary dictionary;
  private final GraphQLScalarType[] scalars;
  private final List<SchemaDirective> directives;
  private final List<SchemaDirectiveWiring> directiveWirings;

  public DynamicGraphQLSchemaProvider(
      SchemaParser schemaParser,
      List<GraphQLResolver<?>> resolvers,
      SchemaStringProvider schemaStringProvider,
      SchemaParserOptions.Builder optionsBuilder,
      SchemaParserDictionary dictionary,
      GraphQLScalarType[] scalars,
      List<SchemaDirective> directives,
      List<SchemaDirectiveWiring> directiveWirings) {
    this.graphQLSchema = schemaParser.makeExecutableSchema();
    this.readOnlySchema = GraphQLSchemaProvider.copyReadOnly(this.graphQLSchema);

    this.resolvers = resolvers;
    this.schemaStringProvider = schemaStringProvider;
    this.optionsBuilder = optionsBuilder;
    this.dictionary = dictionary;
    this.scalars = scalars;
    this.directives = directives;
    this.directiveWirings = directiveWirings;
  }

  public void updateSchema() throws IOException {
    SchemaParserBuilder builder = new SchemaParserBuilder();
    if (nonNull(dictionary)) {
      builder.dictionary(dictionary.getDictionary());
    }
    List<String> schemaStrings = schemaStringProvider.schemaStrings();
    schemaStrings.forEach(builder::schemaString);

    if (scalars != null) {
      builder.scalars(scalars);
    }

    builder.options(optionsBuilder.build());

    if (directives != null) {
      directives.forEach(it -> builder.directive(it.getName(), it.getDirective()));
    }

    if (directiveWirings != null) {
      directiveWirings.forEach(builder::directiveWiring);
    }

    this.graphQLSchema = builder.resolvers(resolvers).build().makeExecutableSchema();
    this.readOnlySchema = GraphQLSchemaProvider.copyReadOnly(this.graphQLSchema);
  }

  @Override
  public GraphQLSchema getSchema() {
    return this.graphQLSchema;
  }

  @Override
  public GraphQLSchema getReadOnlySchema() {
    return this.readOnlySchema;
  }

  @Override
  public GraphQLSchema getSchema(HttpServletRequest request) {
    return this.graphQLSchema;
  }

  @Override
  public GraphQLSchema getSchema(HandshakeRequest request) {
    return this.graphQLSchema;
  }

  @Override
  public GraphQLSchema getReadOnlySchema(HttpServletRequest request) {
    return this.readOnlySchema;
  }
}
