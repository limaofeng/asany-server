package org.jfantasy.framework.dao.jpa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.jfantasy.framework.dao.jpa.PropertyFilter.MatchType;
import org.springframework.data.jpa.domain.Specification;

/**
 * 属性过滤器 构造器
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-04-22 17:55
 */
public class PropertyFilterBuilder {

  private List<PropertyFilter> filters = new ArrayList<>();

  public PropertyFilterBuilder() {}

  public PropertyFilterBuilder(List<PropertyFilter> filters) {
    this.filters = filters;
  }

  public List<PropertyFilter> build() {
    return this.filters;
  }

  /**
   * 等于
   *
   * @param name
   * @param value
   * @param <T>
   * @return
   */
  public <T> PropertyFilterBuilder equal(String name, T value) {
    this.filters.add(new PropertyFilter(MatchType.EQ, name, value));
    return this;
  }

  /** 模糊查询 */
  public PropertyFilterBuilder contains(String name, String value) {
    this.filters.add(new PropertyFilter(MatchType.CONTAINS, name, value));
    return this;
  }

  public PropertyFilterBuilder notContains(String name, String value) {
    this.filters.add(new PropertyFilter(MatchType.NOT_CONTAINS, name, value));
    return this;
  }

  public PropertyFilterBuilder startsWith(String name, String value) {
    this.filters.add(new PropertyFilter(MatchType.STARTS_WITH, name, value + "%"));
    return this;
  }

  public PropertyFilterBuilder notStartsWith(String name, String value) {
    this.filters.add(new PropertyFilter(MatchType.NOT_STARTS_WITH, name, value + "%"));
    return this;
  }

  public PropertyFilterBuilder endsWith(String name, String value) {
    this.filters.add(new PropertyFilter(MatchType.ENDS_WITH, name, "%" + value));
    return this;
  }

  public PropertyFilterBuilder notEndsWith(String name, String value) {
    this.filters.add(new PropertyFilter(MatchType.NOT_ENDS_WITH, name, "%" + value));
    return this;
  }

  /** 小于 */
  public <T> PropertyFilterBuilder lessThan(String name, T value) {
    this.filters.add(new PropertyFilter(MatchType.LT, name, value));
    return this;
  }

  /** 大于 */
  public PropertyFilterBuilder greaterThan(String name, Object value) {
    this.filters.add(new PropertyFilter(MatchType.GT, name, value));
    return this;
  }

  /** 小于等于 */
  public PropertyFilterBuilder lessThanOrEqual(String name, Object value) {
    this.filters.add(new PropertyFilter(MatchType.LTE, name, value));
    return this;
  }

  /** 大于等于 */
  public PropertyFilterBuilder greaterThanOrEqual(String name, Object value) {
    this.filters.add(new PropertyFilter(MatchType.GTE, name, value));
    return this;
  }

  /** in */
  public <T> PropertyFilterBuilder in(String name, T... value) {
    this.filters.add(new PropertyFilter(MatchType.IN, name, value));
    return this;
  }

  /**
   * @param name
   * @param value
   * @param <T>
   * @return
   */
  public <T> PropertyFilterBuilder in(String name, List<T> value) {
    this.filters.add(new PropertyFilter(MatchType.IN, name, value));
    return this;
  }

  /** not in */
  public <T> PropertyFilterBuilder notIn(String name, T... value) {
    this.filters.add(new PropertyFilter(MatchType.NOT_IN, name, value));
    return this;
  }

  /** 不等于 */
  public <T> PropertyFilterBuilder notEqual(String name, T value) {
    this.filters.add(new PropertyFilter(MatchType.NOT_EQUAL, name, value));
    return this;
  }

  /** is null */
  public PropertyFilterBuilder isNull(String name) {
    this.filters.add(new PropertyFilter(MatchType.NULL, name));
    return this;
  }

  /** not null */
  public PropertyFilterBuilder isNotNull(String name) {
    this.filters.add(new PropertyFilter(MatchType.NOT_NULL, name));
    return this;
  }

  /** */
  public PropertyFilterBuilder isEmpty(String name) {
    this.filters.add(new PropertyFilter(MatchType.EMPTY, name));
    return this;
  }

  /** */
  public PropertyFilterBuilder isNotEmpty(String name) {
    this.filters.add(new PropertyFilter(MatchType.NOT_EMPTY, name));
    return this;
  }

  public <Y extends Comparable<? super Y>> PropertyFilterBuilder between(String name, Y x, Y y) {
    this.filters.add(new PropertyFilter(MatchType.BETWEEN, name, x, y));
    return this;
  }

  public PropertyFilterBuilder and(PropertyFilterBuilder... builders) {
    this.filters.add(
        new PropertyFilter(
            MatchType.AND,
            Arrays.stream(builders)
                .map(PropertyFilterBuilder::build)
                .collect(Collectors.toList())));
    return this;
  }

  public PropertyFilterBuilder and(List<PropertyFilter>... filters) {
    this.filters.add(new PropertyFilter(MatchType.AND, Arrays.asList(filters)));
    return this;
  }

  public PropertyFilterBuilder and(Specification... specifications) {
    this.filters.add(new PropertyFilter(MatchType.AND, Arrays.asList(specifications)));
    return this;
  }

  public PropertyFilterBuilder or(List<PropertyFilter>... filters) {
    this.filters.add(new PropertyFilter(MatchType.OR, Arrays.asList(filters)));
    return this;
  }

  public PropertyFilterBuilder or(PropertyFilterBuilder... builders) {
    this.filters.add(
        new PropertyFilter(
            MatchType.OR,
            Arrays.stream(builders)
                .map(PropertyFilterBuilder::build)
                .collect(Collectors.toList())));
    return this;
  }

  public PropertyFilterBuilder or(Specification... specifications) {
    this.filters.add(new PropertyFilter(MatchType.OR, specifications));
    return this;
  }

  public PropertyFilterBuilder not(List<PropertyFilter>... filters) {
    this.filters.add(new PropertyFilter(MatchType.NOT, Arrays.asList(filters)));
    return this;
  }

  public PropertyFilterBuilder not(PropertyFilterBuilder... builders) {
    this.filters.add(
        new PropertyFilter(
            MatchType.NOT,
            Arrays.stream(builders)
                .map(PropertyFilterBuilder::build)
                .collect(Collectors.toList())));
    return this;
  }

  public PropertyFilterBuilder not(Specification... specifications) {
    this.filters.add(new PropertyFilter(MatchType.NOT, specifications));
    return this;
  }
}
