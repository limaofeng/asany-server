package cn.asany.shanhai.core.support.graphql.resolvers.base.utils;

import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

public class MethodArgumentResolver {

    private final static String[] complex = new String[]{"OR", "AND", "NOT"};

    private final static Map<String, PropertyFilter.MatchType> matchTypes = new HashMap<>();
    static {
        matchTypes.put("contains", PropertyFilter.MatchType.CONTAINS);
        matchTypes.put("startsWith", PropertyFilter.MatchType.STARTS_WITH);
        matchTypes.put("endsWith", PropertyFilter.MatchType.ENDS_WITH);
        matchTypes.put("equal", PropertyFilter.MatchType.EQ);
    }

    private static PropertyFilter.MatchType buildMatchType(String name){
        if(ObjectUtil.exists(complex, name)){
           return PropertyFilter.MatchType.valueOf(name);
        }
        return matchTypes.get(name);
    }

    public static PropertyFilterBuilder[] where(Map<String, Object>[] wheres) {
        return Arrays.stream(wheres).map(item -> where(item)).toArray(PropertyFilterBuilder[]::new);
    }

    public static PropertyFilterBuilder where(Map<String, Object> where) {
        List<PropertyFilter> filters = new ArrayList<>();

        for(Map.Entry<String, Object> entry : where.entrySet()){
            if(ObjectUtil.exists(complex, entry.getKey())){
                filters.add(new PropertyFilter(buildMatchType(entry.getKey()), Arrays.stream(where(((Collection<?>) entry.getValue()).stream().toArray(Map[]::new))).map(item -> item.build()).collect(Collectors.toList())));
            }else{
                String[] names = entry.getKey().split("_");
                filters.add(new PropertyFilter(buildMatchType(names.length == 2 ? names[1]: "equal"), names[0], entry.getValue()));
            }
        }

        return new PropertyFilterBuilder(filters);
    }
}
