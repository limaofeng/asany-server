package cn.asany.security.core.domain.converter;

import cn.asany.security.auth.graphql.directive.ConditionOperator;
import cn.asany.security.core.domain.PermissionCondition;
import jakarta.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.StringUtil;

public class ConditionConverter implements AttributeConverter<List<PermissionCondition>, String> {

  @Override
  public String convertToDatabaseColumn(List<PermissionCondition> attribute) {
    if (attribute == null || attribute.isEmpty()) {
      return null;
    }
    Map<String, Map<String, List<String>>> data = new HashMap<>();

    for (PermissionCondition condition : attribute) {
      String operator = condition.getOperator().getValue();
      String key = condition.getResourceType() + "::" + condition.getFieldName();
      List<String> values = condition.getValues();

      if (!data.containsKey(operator)) {
        data.put(operator, new HashMap<>());
      }

      data.get(operator).put(key, values);
    }
    return JSON.serialize(data);
  }

  @Override
  public List<PermissionCondition> convertToEntityAttribute(String dbData) {
    if (StringUtil.isBlank(dbData)) {
      return null;
    }
    List<PermissionCondition> conditions = new ArrayList<>();
    //noinspection unchecked
    Map<String, Map<String, List<String>>> data = JSON.deserialize(dbData, Map.class);

    for (Map.Entry<String, Map<String, List<String>>> operatorEntry : data.entrySet()) {
      ConditionOperator operator = ConditionOperator.fromString(operatorEntry.getKey());

      for (Map.Entry<String, List<String>> conditionEntity : operatorEntry.getValue().entrySet()) {
        String conditionKey = conditionEntity.getKey();
        int index = conditionKey.lastIndexOf("::");

        String resourceType = conditionKey.substring(0, index);
        String property = conditionKey.substring(index + 2);

        List<String> values = conditionEntity.getValue();

        conditions.add(
            PermissionCondition.builder()
                .resourceType(resourceType)
                .fieldName(property)
                .operator(operator)
                .values(values)
                .build());
      }
    }

    return conditions;
  }
}
