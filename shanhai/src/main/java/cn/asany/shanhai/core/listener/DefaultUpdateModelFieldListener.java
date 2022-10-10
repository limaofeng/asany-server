package cn.asany.shanhai.core.listener;

import cn.asany.shanhai.core.event.UpdateModelFieldEvent.UpdateModelFieldEventSource;
import cn.asany.shanhai.core.event.UpdateModelFieldEvent;
import cn.asany.shanhai.core.support.ModelParser;
import cn.asany.shanhai.core.support.graphql.DynamicGraphQLSchemaProvider;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class DefaultUpdateModelFieldListener implements ApplicationListener<UpdateModelFieldEvent> {

  @Autowired private ModelParser modelParser;

  @Autowired(required = false)
  private DynamicGraphQLSchemaProvider schemaProvider;

  @SneakyThrows
  @Override
  public void onApplicationEvent(UpdateModelFieldEvent event) {
    UpdateModelFieldEventSource source = (UpdateModelFieldEventSource) event.getSource();
    modelParser.updateModelField(source.getModelId(), source.getField());
    schemaProvider.updateSchema();
  }
}
