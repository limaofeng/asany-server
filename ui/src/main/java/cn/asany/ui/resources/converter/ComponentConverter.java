package cn.asany.ui.resources.converter;

import cn.asany.ui.resources.bean.Component;
import cn.asany.ui.resources.graphql.type.ComponentCreateInput;
import cn.asany.ui.resources.graphql.type.ComponentUpdateInput;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * 组件对象 转换器
 *
 * @author limaofeng
 */
@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ComponentConverter {

  Component toComponent(ComponentCreateInput input);

  Component toComponent(ComponentUpdateInput input);
}
