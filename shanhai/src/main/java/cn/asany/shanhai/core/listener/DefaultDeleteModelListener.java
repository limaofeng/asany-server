package cn.asany.shanhai.core.listener;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.event.DeleteModelEvent;
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
public class DefaultDeleteModelListener implements ApplicationListener<DeleteModelEvent> {

  @Autowired private ModelParser modelParser;

  @Autowired private GraphQLReloadSchemaProvider schemaProvider;

  @SneakyThrows
  @Override
  public void onApplicationEvent(DeleteModelEvent event) {
    Model source = (Model) event.getSource();
    StopWatch sw = StopWatchHolder.get();

    modelParser.deleteModel(source);

    schemaProvider.updateSchema();

    log.info(sw.prettyPrint());
  }
}
