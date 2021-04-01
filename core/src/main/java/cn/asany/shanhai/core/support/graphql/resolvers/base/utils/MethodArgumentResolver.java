package cn.asany.shanhai.core.support.graphql.resolvers.base.utils;

import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MethodArgumentResolver {

    private final static String[] complex = new String[]{"OR", "AND", "NOT"};

    public static List<PropertyFilter>[] where(Map<String, Object>[] wheres) {
        return Arrays.stream(wheres).map(item -> where(item)).toArray(List[]::new);
    }

    public static PropertyFilterBuilder where(Map<String, Object> where) {
        List<PropertyFilter> filters = new ArrayList<>();

        for(Map.Entry<String, Object> entry : where.entrySet()){
            if(ObjectUtil.exists(complex, entry.getKey())){
                filters.add(new PropertyFilter(PropertyFilter.MatchType.valueOf(entry.getKey()), where((Map<String, Object>[]) entry.getValue())));
            }else{
                String[] names = entry.getKey().split("_");
                filters.add(new PropertyFilter(PropertyFilter.MatchType.EQ, names[0], entry.getValue()));
            }
        }

        for (String name : complex) {
            if (where.containsKey(name)) {

            }
        }
        return builder;
    }
}
