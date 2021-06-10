package cn.asany.nuwa.app.converter;

import cn.asany.nuwa.app.bean.Application;
import cn.asany.nuwa.template.bean.ApplicationTemplate;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",builder = @Builder,unmappedTargetPolicy = ReportingPolicy.IGNORE,nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ApplicationConverter {

    Application toApplication(ApplicationTemplate input);

}
