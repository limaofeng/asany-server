/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.security.auth.graphql.directive;

import java.util.List;
import java.util.Map;
import net.asany.jfantasy.framework.dao.MatchType;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.error.ValidationException;

public enum ConditionOperator {
  /** 检查是否相等 */
  EQUALS("Equals", MatchType.EQ),
  /** 检查是否不相等 */
  NOT_EQUALS("NotEquals", MatchType.NOT_EQUAL),
  /** 检查是否小于 */
  LESS_THAN("LessThan", MatchType.LT),
  /** 检查是否大于 */
  GREATER_THAN("GreaterThan", MatchType.GT),
  /** 检查是否小于等于 */
  LESS_THAN_OR_EQUAL("LessThanOrEqual", MatchType.LTE),
  /** 检查是否大于等于 */
  GREATER_THAN_OR_EQUAL("GreaterThanOrEqual", MatchType.GTE),
  /** 检查是否在集合中 */
  IN("In", MatchType.IN),
  /** 检查是否不在集合中 */
  NOT_IN("NotIn", MatchType.NOT_IN),
  /** 检查是否为空 */
  IS_NULL("IsNull", MatchType.NULL),
  /** 检查是否不为空 */
  NOT_NULL("IsNotNull", MatchType.NOT_NULL),
  /** 检查字符串是否包含子串 */
  CONTAINS("Contains", MatchType.CONTAINS),
  /** 检查字符串是否不包含子串 */
  NOT_CONTAINS("NotContains", MatchType.NOT_CONTAINS),
  /** 检查字符串是否以子串开头 */
  STARTS_WITH("StartsWith", MatchType.STARTS_WITH),
  /** 检查字符串是否不以子串开头 */
  NOT_STARTS_WITH("NotStartsWith", MatchType.NOT_STARTS_WITH),
  /** 检查字符串是否以子串结尾 */
  ENDS_WITH("EndsWith", MatchType.ENDS_WITH),
  /** 检查字符串是否不以子串结尾 */
  NOT_ENDS_WITH("NotEndsWith", MatchType.NOT_ENDS_WITH);

  private final String operator;
  private final MatchType matchType;

  ConditionOperator(String operator, MatchType matchType) {
    this.operator = operator;
    this.matchType = matchType;
  }

  public PropertyFilter execute(PropertyFilter filter, String name, Object value) {
    return matchType.build(filter, name, value);
  }

  public String getValue() {
    return this.operator;
  }

  /**
   * 通过条件运算符的字符串值获取对应的枚举值。
   *
   * @param operator 条件运算符的字符串值
   * @return 对应的枚举值，如果找不到则返回 null
   */
  public static ConditionOperator fromString(String operator) {
    for (ConditionOperator value : ConditionOperator.values()) {
      if (value.operator.equalsIgnoreCase(operator)) {
        return value;
      }
    }
    throw new ValidationException("不支持的条件运算符：" + operator);
  }

  public void buildWheres(
      Map<Class<?>, List<PropertyFilter>> wheres, Map<String, List<String>> conditions) {
    for (Map.Entry<String, List<String>> entry : conditions.entrySet()) {
      Class<?> entityClass = null;
      String name = entry.getKey();
      List<PropertyFilter> rootPropertyFilter = wheres.get(entityClass);
      PropertyFilter filter = buildPropertyFilter(entityClass, name, entry.getValue());
      rootPropertyFilter.add(filter);
    }
  }

  private PropertyFilter buildPropertyFilter(
      Class<?> entityClass, String name, List<String> value) {
    //    this.consumer.accept(filter);
    return null;
  }
}
