package cn.asany.shanhai.core.support.graphql.resolvers.base.utils;

import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;

import java.util.*;
import java.util.stream.Collectors;

public class MethodArgumentResolver {

    private final static String[] COMPLEX = new String[]{"OR", "AND", "NOT"};

    private final static Map<String, PropertyFilter.MatchType> MATCH_TYPES = new HashMap<>();
    static {
        MATCH_TYPES.put("contains", PropertyFilter.MatchType.CONTAINS);
        MATCH_TYPES.put("startsWith", PropertyFilter.MatchType.STARTS_WITH);
        MATCH_TYPES.put("endsWith", PropertyFilter.MatchType.ENDS_WITH);
        MATCH_TYPES.put("equal", PropertyFilter.MatchType.EQ);
    }

    private static PropertyFilter.MatchType buildMatchType(String name){
        if(ObjectUtil.exists(COMPLEX, name)){
           return PropertyFilter.MatchType.valueOf(name);
        }
        return MATCH_TYPES.get(name);
    }

    public static PropertyFilterBuilder[] where(Map<String, Object>[] wheres) {
        return Arrays.stream(wheres).map(MethodArgumentResolver::where).toArray(PropertyFilterBuilder[]::new);
    }

    public static PropertyFilterBuilder where(Map<String, Object> where) {
        List<PropertyFilter> filters = new ArrayList<>();

        for(Map.Entry<String, Object> entry : where.entrySet()){
            if(ObjectUtil.exists(COMPLEX, entry.getKey())){
                filters.add(new PropertyFilter(buildMatchType(entry.getKey()), Arrays.stream(where(((Collection<Map>) entry.getValue()).toArray(new Map[0]))).map(PropertyFilterBuilder::build).collect(Collectors.toList())));
            }else{
                String[] names = entry.getKey().split("_");
                filters.add(new PropertyFilter(buildMatchType(names.length == 2 ? names[1]: "equal"), names[0], entry.getValue()));
            }
        }

        return new PropertyFilterBuilder(filters);
    }
}
