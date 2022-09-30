package cn.asany.shanhai.core.support.graphql.config;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.domain.ModelEndpoint;
import cn.asany.shanhai.core.domain.ModelEndpointArgument;
import cn.asany.shanhai.core.domain.ModelField;
import cn.asany.shanhai.core.domain.enums.ModelEndpointType;
import cn.asany.shanhai.core.support.ModelParser;
import cn.asany.shanhai.core.support.model.FieldType;
import cn.asany.shanhai.core.support.model.FieldTypeRegistry;
import graphql.kickstart.tools.TypeDefinitionFactory;
import graphql.language.*;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class CustomTypeDefinitionFactory implements TypeDefinitionFactory {

  private final ModelParser modelParser;

  public CustomTypeDefinitionFactory(ModelParser modelParser) {
    this.modelParser = modelParser;
  }

  @NotNull
  @Override
  public List<Definition<?>> create(@NotNull List<Definition<?>> list) {
    List<Definition<?>> allType = new ArrayList<>();

    FieldTypeRegistry fieldTypeRegistry = modelParser.getFieldTypeRegistry();

    ObjectTypeExtensionDefinition.Builder queryBuilder =
        ObjectTypeExtensionDefinition.newObjectTypeExtensionDefinition().name("Query");

    for (Model model : modelParser.getModels()) {
      ObjectTypeDefinition.Builder typeBuilder =
          ObjectTypeDefinition.newObjectTypeDefinition().name(model.getCode());
      for (ModelField field : model.getFields()) {
        Type type = getType(field.getType(), field.getUnique(), field.getList(), fieldTypeRegistry);
        typeBuilder.fieldDefinition(new FieldDefinition(field.getCode(), type));
      }

      allType.add(typeBuilder.build());

      for (ModelEndpoint endpoint : model.getEndpoints()) {
        if (ModelEndpointType.LIST == endpoint.getType()
            || endpoint.getType() == ModelEndpointType.GET
            || endpoint.getType() == ModelEndpointType.CONNECTION) {

          FieldDefinition.Builder fieldBuilder =
              FieldDefinition.newFieldDefinition()
                  .name(endpoint.getCode())
                  .description(
                      new Description(
                          endpoint.getName() + "<br/>" + endpoint.getDescription(),
                          new SourceLocation(0, 0),
                          true));

          for (ModelEndpointArgument argument : endpoint.getArguments()) {
            fieldBuilder.inputValueDefinition(
                InputValueDefinition.newInputValueDefinition()
                    .name(argument.getName())
                    .description(
                        new Description(
                            argument.getDescription() + "<br/>" + endpoint.getDescription(),
                            new SourceLocation(0, 0),
                            true))
                    .type(
                        getType(
                            argument.getType(),
                            argument.getRequired(),
                            argument.getList(),
                            fieldTypeRegistry))
                    .build());
          }

          queryBuilder.fieldDefinition(fieldBuilder.build());
        } else {

        }
      }
    }

    //            .build();

    //            .fieldDefinition(
    //                FieldDefinition.newFieldDefinition()
    //                    .name("myTypes")
    //                    .type(TypeName.newTypeName("MyType").build())
    //                    .build())
    //            .build();

    allType.add(queryBuilder.build());
    return allType;
  }

  public Type getType(
      String typeStr, boolean unique, boolean list, FieldTypeRegistry fieldTypeRegistry) {
    FieldType fieldType = fieldTypeRegistry.getType(typeStr);

    Type type = new TypeName(fieldType.getGraphQLType());

    type = unique ? NonNullType.newNonNullType(type).build() : type;

    if (list) {
      type = ListType.newListType(type).build();
      type = unique ? NonNullType.newNonNullType(type).build() : type;
    }

    return type;
  }
}
