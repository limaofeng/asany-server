package cn.asany.security.core.graphql.inputs;

import lombok.Builder;
import lombok.Data;
import cn.asany.security.core.graphql.enums.ResultTypeScope;

import java.util.List;

@Data
@Builder
public class ScopeTypeResult {
    // 类型
    private ResultTypeScope type;
    // key
    private String key;
    // value
    // private String value;
    // 中文描述
    private String title;
    // 支持的目标类型
    private List<SecurityTypeData> targetTypes;

}
