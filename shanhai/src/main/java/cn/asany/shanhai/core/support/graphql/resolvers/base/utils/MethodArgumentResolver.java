package cn.asany.shanhai.core.support.graphql.resolvers.base.utils;

import java.util.*;
import java.util.stream.Collectors;
import org.jfantasy.framework.dao.MatchType;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyPredicate;
import org.jfantasy.framework.util.common.ObjectUtil;

/**
 * 方法参数解析器
 *
 * @author limaofeng
 */
public class MethodArgumentResolver {

  private static final String[] COMPLEX = new String[] {"OR", "AND", "NOT"};

  private static final Map<String, MatchType> MATCH_TYPES = new HashMap<>();

  static {
    MATCH_TYPES.put("contains", MatchType.CONTAINS);
    MATCH_TYPES.put("startsWith", MatchType.STARTS_WITH);
    MATCH_TYPES.put("endsWith", MatchType.ENDS_WITH);
    MATCH_TYPES.put("equal", MatchType.EQ);
  }

  private static MatchType buildMatchType(String name) {
    if (ObjectUtil.exists(COMPLEX, name)) {
      return MatchType.valueOf(name);
    }
    return MATCH_TYPES.get(name);
  }

  public static PropertyFilter[] where(Map<String, Object>[] wheres) {
    return Arrays.stream(wheres).map(MethodArgumentResolver::where).toArray(PropertyFilter[]::new);
  }

  public static PropertyFilter where(Map<String, Object> where) {
    List<PropertyPredicate> predicates = new ArrayList<>();

    for (Map.Entry<String, Object> entry : where.entrySet()) {
      if (ObjectUtil.exists(COMPLEX, entry.getKey())) {
        predicates.add(
            new PropertyPredicate(
                buildMatchType(entry.getKey()),
                Arrays.stream(where(((Collection<Map>) entry.getValue()).toArray(new Map[0])))
                    .map(PropertyFilter::build)
                    .collect(Collectors.toList())));
      } else {
        String[] names = entry.getKey().split("_");
        predicates.add(
            new PropertyPredicate(
                buildMatchType(names.length == 2 ? names[1] : "equal"),
                names[0],
                entry.getValue()));
      }
    }

    return PropertyFilter.newFilter(predicates);
  }
}
