package cn.asany.security.core.graphql.models;

import cn.asany.security.core.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.graphql.inputs.WhereInput;

public class UserWhereInput extends WhereInput<UserWhereInput, User> {

}
