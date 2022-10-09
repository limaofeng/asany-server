package cn.asany.shanhai.core.listener;

import cn.asany.shanhai.core.event.DeleteModelFieldEvent;
import cn.asany.shanhai.core.event.DeleteModelFieldEvent.DeleteModelFieldSource;
import cn.asany.shanhai.core.support.ModelParser;
import cn.asany.shanhai.core.support.graphql.DynamicGraphQLSchemaProvider;
import lombok.SneakyThrows;
import org.hibernate.sql.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class DefaultDeleteModelFieldListener implements ApplicationListener<DeleteModelFieldEvent> {

  @Autowired private ModelParser modelParser;

  @Autowired(required = false)
  private DynamicGraphQLSchemaProvider schemaProvider;

  @SneakyThrows
  @Override
  public void onApplicationEvent(DeleteModelFieldEvent event) {
    DeleteModelFieldSource source = (DeleteModelFieldSource) event.getSource();
    modelParser.deleteModelField(source.getModelId(), source.getField());
    schemaProvider.updateSchema();
  }
}
