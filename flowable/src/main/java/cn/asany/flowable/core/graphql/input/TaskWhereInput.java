package cn.asany.flowable.core.graphql.input;

import cn.asany.flowable.core.filters.TaskPropertyFilter;
import org.flowable.task.api.TaskInfo;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.graphql.inputs.WhereInput;

/**
 * 任务查询条件
 *
 * @author limaofeng
 */
public class TaskWhereInput extends WhereInput<TaskWhereInput, TaskInfo> {

  public TaskWhereInput() {
    super();
    ((TaskPropertyFilter) this.filter)
        .getPropertyNames()
        .forEach(
            name -> {
              //              this.register(name, new NothingTypeConverter<>());
            });
  }

  @Override
  protected PropertyFilter initPropertyFilter() {
    return TaskPropertyFilter.newFilter(true);
  }
}
