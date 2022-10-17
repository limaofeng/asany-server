package cn.asany.shanhai.core.listener;

import cn.asany.shanhai.core.event.DeleteModelFieldEvent;
import cn.asany.shanhai.core.event.DeleteModelFieldEvent.DeleteModelFieldSource;
import cn.asany.shanhai.core.support.ModelParser;
import cn.asany.shanhai.core.support.graphql.GraphQLReloadSchemaProvider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Component
public class DefaultDeleteModelFieldListener implements ApplicationListener<DeleteModelFieldEvent> {

  @Autowired private ModelParser modelParser;

  @Autowired private GraphQLReloadSchemaProvider schemaProvider;

  @SneakyThrows
  @Override
  public void onApplicationEvent(DeleteModelFieldEvent event) {
    DeleteModelFieldSource source = (DeleteModelFieldSource) event.getSource();
    StopWatch sw = StopWatchHolder.get();

    modelParser.deleteModelField(source.getModelId(), source.getField());

    schemaProvider.updateSchema();

    log.info(sw.prettyPrint());
  }
}
