package cn.asany.security.core.graphql.models;

import lombok.Data;
import cn.asany.security.core.graphql.inputs.GrantPermissionByUserInput;

/**
 * @author limaofeng
 * @version V1.0
 * @Description: TODO
 * @date 2019-06-17 15:36
 */
@Data
public class BusinessScopeInput {



    private String name;

    private String code;
    private Boolean enabled;


}
