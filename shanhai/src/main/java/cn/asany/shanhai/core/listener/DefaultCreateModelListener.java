package cn.asany.shanhai.core.listener;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.event.CreateModelEvent;
import cn.asany.shanhai.core.support.ModelParser;
import cn.asany.shanhai.core.support.graphql.DynamicGraphQLSchemaProvider;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class DefaultCreateModelListener implements ApplicationListener<CreateModelEvent> {

  @Autowired private ModelParser modelParser;

  @Autowired(required = false)
  private DynamicGraphQLSchemaProvider schemaProvider;

  @SneakyThrows
  @Override
  public void onApplicationEvent(CreateModelEvent event) {
    Model source = (Model) event.getSource();
    modelParser.createModel(source);

    schemaProvider.updateSchema();
  }
}
