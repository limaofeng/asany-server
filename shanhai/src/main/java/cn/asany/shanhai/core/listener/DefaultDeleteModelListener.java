package cn.asany.shanhai.core.listener;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.event.DeleteModelEvent;
import cn.asany.shanhai.core.event.DeleteModelFieldEvent;
import cn.asany.shanhai.core.event.DeleteModelFieldEvent.DeleteModelFieldSource;
import cn.asany.shanhai.core.support.ModelParser;
import cn.asany.shanhai.core.support.graphql.DynamicGraphQLSchemaProvider;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class DefaultDeleteModelListener implements ApplicationListener<DeleteModelEvent> {

  @Autowired private ModelParser modelParser;

  @Autowired(required = false)
  private DynamicGraphQLSchemaProvider schemaProvider;

  @SneakyThrows
  @Override
  public void onApplicationEvent(DeleteModelEvent event) {
      Model source = (Model) event.getSource();
    modelParser.deleteModel(source);
    schemaProvider.updateSchema();
  }
}
