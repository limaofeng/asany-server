package cn.asany.shanhai.gateway.util;

import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.ognl.OgnlUtil;
import org.jfantasy.framework.util.reflect.Property;

import java.util.*;
import java.util.stream.Collectors;

public class Equator {

    public static boolean isEquals(Object left, Object right) {
        return false;
    }

    public static List<DiffObject> diff(Object left, Object right) {
        return diff(left, right, "");
    }

    public static List<DiffObject> diff(Object left, Object right, String parentPath) {
        Property[] properties = ClassUtil.getPropertys(left.getClass());
        List<DiffObject> diffObjects = new ArrayList<>();
        for (Property property : properties) {
            if (property.isTransient()) {
                continue;
            }
            String propertyPath = parentPath + "/" + property.getName();
            Object childByLeft = property.getValue(left);
            Object childByRight = property.getValue(right);
            DiffObject.DiffObjectBuilder builder = DiffObject.builder().status(DiffObject.DiffStatus.M).path(propertyPath).prev(childByLeft).current(childByRight);
            Class childType = property.getPropertyType();
            if (ClassUtil.isArray(childType)) {
                System.out.println("xx");
            } else if (ClassUtil.isList(childType)) {
                List<DiffObject> children = new ArrayList<>();
                Class entityClass = ClassUtil.getMethodGenericReturnType(property.getReadMethod().getMethod());
                if (ClassUtil.getProperty(entityClass, "id") != null) {
                    Map prevMap = toMap((List) childByLeft, "id");
                    Map currentMap = toMap((List) childByRight, "id");

                    Set appendItems = differenceSet(prevMap.keySet(), currentMap.keySet());
                    Set deletedItems = differenceSet(prevMap.keySet(), currentMap.keySet());
                    Set modifiedKeys = intersection(prevMap.keySet(), currentMap.keySet());

                    children.addAll((List) appendItems.stream().map(key -> {
                        Object prev = prevMap.get(key);
                        int index = ObjectUtil.indexOf((List) childByRight, currentMap.get(key));
                        String path = propertyPath + "[" + index + "]";
                        return DiffObject.builder().path(path).status(DiffObject.DiffStatus.A).prev(prev).build();
                    }).collect(Collectors.toList()));

                    children.addAll((List) deletedItems.stream().map(key -> {
                        Object prev = prevMap.get(key);
                        int index = ObjectUtil.indexOf((List) childByRight, currentMap.get(key));
                        String path = propertyPath + "[" + index + "]";
                        return DiffObject.builder().path(path).status(DiffObject.DiffStatus.D).prev(prev).build();
                    }).collect(Collectors.toList()));

                    children.addAll((List) modifiedKeys.stream().filter((key) -> !prevMap.get(key).equals(currentMap.get(key))).map(key -> {
                        Object prev = prevMap.get(key);
                        Object current = currentMap.get(key);
                        int index = ObjectUtil.indexOf((List) childByRight, currentMap.get(key));
                        String path = propertyPath + "[" + index + "]";
                        return DiffObject.builder().path(path).status(DiffObject.DiffStatus.M).prev(prev).current(current).diffObjects(diff(prev, current, path)).build();
                    }).collect(Collectors.toList()));


                } else {
                    Set prev = new HashSet((List) childByLeft);
                    Set current = new HashSet((List) childByRight);
                    Set appendItems = differenceSet(prev, current);
                    Set deletedItems = differenceSet(current, prev);
                }

                if (!children.isEmpty()) {
                    diffObjects.add(builder.diffObjects(children).build());
                }
            } else if (ClassUtil.isMap(childType)) {
                List<DiffObject> children = new ArrayList<>();
                Map prevMap = ObjectUtil.defaultValue((Map) childByLeft, new HashMap());
                Map currentMap = ObjectUtil.defaultValue((Map) childByRight, new HashMap());
                Set appendItems = differenceSet(currentMap.keySet(), prevMap.keySet());
                Set deletedItems = differenceSet(prevMap.keySet(), currentMap.keySet());
                Set modifiedKeys = intersection(prevMap.keySet(), currentMap.keySet());

                children.addAll((List) appendItems.stream().map(key -> {
                    String path = propertyPath + "[" + key + "]";
                    Object current = currentMap.get(key);
                    return DiffObject.builder().path(path).status(DiffObject.DiffStatus.A).current(current).build();
                }).collect(Collectors.toList()));

                children.addAll((List) deletedItems.stream().map(key -> {
                    String path = propertyPath + "[" + key + "]";
                    Object prev = prevMap.get(key);
                    Object current = currentMap.get(key);
                    return DiffObject.builder().path(path).status(DiffObject.DiffStatus.D).prev(prev).current(current).build();
                }).collect(Collectors.toList()));

                children.addAll((List) modifiedKeys.stream().filter((key) -> !prevMap.get(key).equals(currentMap.get(key))).map(key -> {
                    String path = propertyPath + "[" + key + "]";
                    Object prev = prevMap.get(key);
                    Object current = currentMap.get(key);
                    return DiffObject.builder().path(path).status(DiffObject.DiffStatus.M).prev(prev).current(current).diffObjects(diff(prev, current, path)).build();
                }).collect(Collectors.toList()));

                if (!children.isEmpty()) {
                    diffObjects.add(builder.diffObjects(children).build());
                }
            } else if (ClassUtil.isBasicType(childType)) {
                if (childByLeft == childByRight) {
                    continue;
                }
                if (childByLeft == null || !childByLeft.equals(childByRight)) {
                    diffObjects.add(builder.build());
                }
            } else {
                List<DiffObject> children = diff(childByLeft, childByRight, propertyPath);
                if (!children.isEmpty()) {
                    diffObjects.add(builder.diffObjects(children).build());
                }
            }
        }
        return diffObjects;
    }

    public static <PK, T> Map<PK, T> toMap(List<T> list, String fieldName) {
        Map<PK, T> map = new LinkedHashMap<>();
        for (Object t : list) {
            map.put(OgnlUtil.getInstance().getValue(fieldName, t), (T) t);
        }
        return map;
    }

    public static Set<String> intersection(Set<String> left, Set<String> right) {
        Set result = new HashSet();
        result.addAll(left);
        result.retainAll(right);
        return result;
    }

    public static Set<String> differenceSet(Set<String> left, Set<String> right) {
        Set result = new HashSet();
        result.addAll(left);
        result.removeAll(right);
        return result;
    }

    public static Set<String> union(Set<String> left, Set<String> right) {
        Set<String> result = new HashSet();
        result.addAll(left);
        result.addAll(right);
        return result;
    }

}
