package cn.asany.shanhai.core.listener;

import cn.asany.shanhai.core.event.CreateModelFieldEvent;
import cn.asany.shanhai.core.support.ModelParser;
import cn.asany.shanhai.core.support.graphql.DynamicGraphQLSchemaProvider;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import cn.asany.shanhai.core.event.CreateModelFieldEvent.CreateModelFieldSource;

@Component
public class DefaultCreateModelFieldListener implements ApplicationListener<CreateModelFieldEvent> {

  @Autowired private ModelParser modelParser;

  @Autowired(required = false)
  private DynamicGraphQLSchemaProvider schemaProvider;

  @SneakyThrows
  @Override
  public void onApplicationEvent(CreateModelFieldEvent event) {
    CreateModelFieldSource source = (CreateModelFieldSource) event.getSource();
    modelParser.createModelField(source.getModelId(), source.getField());

    schemaProvider.updateSchema();
  }
}
