package cn.asany.shanhai.core.support.graphql.config;

import cn.asany.shanhai.core.support.ModelParser;
import graphql.kickstart.tools.TypeDefinitionFactory;
import graphql.language.Definition;
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
    return modelParser.getDefinitions();
  }
}
