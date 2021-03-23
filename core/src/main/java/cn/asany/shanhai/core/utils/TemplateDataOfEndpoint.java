package cn.asany.shanhai.core.utils;

import cn.asany.shanhai.core.bean.ModelEndpoint;
import cn.asany.shanhai.core.bean.ModelEndpointReturnType;
import lombok.AllArgsConstructor;
import org.jfantasy.framework.util.common.StringUtil;

@AllArgsConstructor
public class TemplateDataOfEndpoint {
    private ModelEndpoint endpoint;

    public String getCode() {
        return endpoint.getCode();
    }

    public String getName() {
        return endpoint.getName();
    }

    public String getDescription() {
        return endpoint.getDescription();
    }

    public String getReturnType() {
        ModelEndpointReturnType returnType = endpoint.getReturnType();
        return StringUtil.upperCaseFirst(returnType.getType().getCode());
    }

}
