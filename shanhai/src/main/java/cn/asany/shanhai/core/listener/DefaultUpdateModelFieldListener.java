package cn.asany.shanhai.core.listener;

import cn.asany.shanhai.core.event.UpdateModelFieldEvent;
import cn.asany.shanhai.core.event.UpdateModelFieldEvent.UpdateModelFieldEventSource;
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
public class DefaultUpdateModelFieldListener implements ApplicationListener<UpdateModelFieldEvent> {

  @Autowired private ModelParser modelParser;

  @Autowired private GraphQLReloadSchemaProvider schemaProvider;

  @SneakyThrows
  @Override
  public void onApplicationEvent(UpdateModelFieldEvent event) {
    UpdateModelFieldEventSource source = (UpdateModelFieldEventSource) event.getSource();
    StopWatch sw = StopWatchHolder.get();

    modelParser.updateModelField(source.getModelId(), source.getField());

    sw.start("更新 Schema");
    schemaProvider.updateSchema();
    sw.stop();

    log.info(sw.prettyPrint());
  }
}
