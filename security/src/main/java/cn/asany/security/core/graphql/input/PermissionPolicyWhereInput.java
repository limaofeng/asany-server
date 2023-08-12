package cn.asany.security.core.graphql.input;

import cn.asany.security.core.domain.PermissionPolicy;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.inputs.WhereInput;

@Data
@EqualsAndHashCode(callSuper = true)
public class PermissionPolicyWhereInput
    extends WhereInput<PermissionPolicyWhereInput, PermissionPolicy> {}
