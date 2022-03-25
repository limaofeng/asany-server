package cn.asany.shanhai.gateway.util;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.ognl.OgnlUtil;
import org.jfantasy.framework.util.reflect.Property;

public class Equator {

  public static boolean isEquals(Object left, Object right) {
    return false;
  }

  public static List<DiffObject> diff(Object left, Object right, Structure structure) {
    return diff(left, right, structure, "");
  }

  public static List<DiffObject> diff(Object left, Object right) {
    return diff(left, right, Structure.LIST);
  }

  private static List<DiffObject> diff(
      Object left, Object right, Structure structure, String parentPath) {
    Property[] properties = ClassUtil.getProperties(left.getClass());
    List<DiffObject> diffObjects = new ArrayList<>();
    for (Property property : properties) {
      if (property.isTransient()) {
        continue;
      }
      String propertyPath = parentPath + "/" + property.getName();
      Object childByLeft = property.getValue(left);
      Object childByRight = property.getValue(right);
      DiffObject.DiffObjectBuilder builder =
          DiffObject.builder()
              .status(DiffObject.DiffStatus.M)
              .path(propertyPath)
              .prev(childByLeft)
              .current(childByRight);
      Class childType = property.getPropertyType();
      if (ClassUtil.isArray(childType)) {
        System.out.println("xx");
      } else if (ClassUtil.isList(childType)) {
        List<DiffObject> children = new ArrayList<>();
        Class entityClass =
            ClassUtil.getMethodGenericReturnType(property.getReadMethod().getMethod());
        if (ClassUtil.getProperty(entityClass, "id") != null) {
          Map prevMap = toMap((List) childByLeft, "id");
          Map currentMap = toMap((List) childByRight, "id");

          Set<String> appendItems = differenceSet(prevMap.keySet(), currentMap.keySet());
          Set<String> deletedItems = differenceSet(prevMap.keySet(), currentMap.keySet());
          Set<String> modifiedKeys = intersection(prevMap.keySet(), currentMap.keySet());

          children.addAll(
              added(
                  appendItems,
                  prevMap,
                  currentMap,
                  (item) -> {
                    int index = ObjectUtil.indexOf((List) childByRight, item.right);
                    return propertyPath + "[" + index + "]";
                  }));

          children.addAll(
              delete(
                  deletedItems,
                  prevMap,
                  currentMap,
                  (item) -> {
                    int index = ObjectUtil.indexOf((List) childByRight, item.right);
                    return propertyPath + "[" + index + "]";
                  }));

          children.addAll(
              modified(
                  modifiedKeys,
                  prevMap,
                  currentMap,
                  (item) -> {
                    int index = ObjectUtil.indexOf((List) childByRight, item.right);
                    return propertyPath + "[" + index + "]";
                  },
                  structure));

        } else {
          Set prev = new HashSet((List) childByLeft);
          Set current = new HashSet((List) childByRight);
          Set appendItems = differenceSet(prev, current);
          Set deletedItems = differenceSet(current, prev);
        }

        if (!children.isEmpty()) {
          joining(diffObjects, builder, children, structure);
        }
      } else if (ClassUtil.isMap(childType)) {
        List<DiffObject> children = new ArrayList<>();
        Map prevMap = ObjectUtil.defaultValue((Map) childByLeft, new HashMap());
        Map currentMap = ObjectUtil.defaultValue((Map) childByRight, new HashMap());

        Set appendItems = differenceSet(currentMap.keySet(), prevMap.keySet());
        Set deletedItems = differenceSet(prevMap.keySet(), currentMap.keySet());
        Set modifiedKeys = intersection(prevMap.keySet(), currentMap.keySet());

        children.addAll(
            added(appendItems, prevMap, currentMap, (item) -> propertyPath + "[" + item.key + "]"));

        children.addAll(
            delete(
                deletedItems, prevMap, currentMap, (item) -> propertyPath + "[" + item.key + "]"));

        children.addAll(
            modified(
                modifiedKeys,
                prevMap,
                currentMap,
                (item) -> propertyPath + "[" + item.key + "]",
                structure));

        if (!children.isEmpty()) {
          joining(diffObjects, builder, children, structure);
        }
      } else if (ClassUtil.isBasicType(childType)) {
        if (childByLeft == childByRight) {
          continue;
        }
        if (childByLeft == null || !childByLeft.equals(childByRight)) {
          diffObjects.add(builder.build());
        }
      } else {
        List<DiffObject> children = diff(childByLeft, childByRight, structure, propertyPath);
        if (!children.isEmpty()) {
          joining(diffObjects, builder, children, structure);
        }
      }
    }
    return diffObjects;
  }

  @Data
  @Builder
  public static class CompareItem {
    Object left;
    Object right;
    String key;
  }

  private static List<DiffObject> added(
      Set<String> items,
      Map<String, Object> prevMap,
      Map<String, Object> currentMap,
      Function<CompareItem, String> other) {
    return items.stream()
        .map(
            key -> {
              Object prev = prevMap.get(key);
              Object current = currentMap.get(key);
              String path =
                  other.apply(
                      CompareItem.builder()
                          .key(key)
                          .left(currentMap.get(key))
                          .right(current)
                          .build());
              return DiffObject.builder()
                  .path(path)
                  .status(DiffObject.DiffStatus.A)
                  .prev(prev)
                  .current(current)
                  .build();
            })
        .collect(Collectors.toList());
  }

  private static List<DiffObject> modified(
      Set<String> items,
      Map<String, Object> prevMap,
      Map<String, Object> currentMap,
      Function<CompareItem, String> other,
      Structure structure) {
    List<DiffObject> diffObjects = new ArrayList<>();
    items.stream()
        .map(
            key ->
                CompareItem.builder()
                    .key(key)
                    .left(prevMap.get(key))
                    .right(currentMap.get(key))
                    .build())
        .filter(item -> !item.left.equals(item.right))
        .forEach(
            item -> {
              Object prev = item.left;
              Object current = item.right;
              String path = other.apply(item);
              DiffObject.DiffObjectBuilder builder =
                  DiffObject.builder()
                      .path(path)
                      .status(DiffObject.DiffStatus.M)
                      .prev(prev)
                      .current(current);
              List<DiffObject> children = diff(prev, current, structure, path);
              if (!children.isEmpty()) {
                joining(diffObjects, builder, children, structure);
              }
            });
    return diffObjects;
  }

  private static List<DiffObject> delete(
      Set<String> items,
      Map<String, Object> prevMap,
      Map<String, Object> currentMap,
      Function<CompareItem, String> other) {
    return items.stream()
        .map(
            key -> {
              Object prev = prevMap.get(key);
              Object current = currentMap.get(key);
              String path =
                  other.apply(
                      CompareItem.builder()
                          .key(key)
                          .left(currentMap.get(key))
                          .right(current)
                          .build());
              return DiffObject.builder()
                  .path(path)
                  .status(DiffObject.DiffStatus.D)
                  .prev(prev)
                  .build();
            })
        .collect(Collectors.toList());
  }

  private static void joining(
      List<DiffObject> diffObjects,
      DiffObject.DiffObjectBuilder builder,
      List<DiffObject> children,
      Structure structure) {
    if (structure == Structure.LIST) {
      diffObjects.addAll(children);
    } else {
      diffObjects.add(builder.current(children).build());
    }
  }

  public static <PK, T> Map<PK, T> toMap(List<T> list, String fieldName) {
    Map<PK, T> map = new LinkedHashMap<>();
    for (T t : list) {
      map.put(OgnlUtil.getInstance().getValue(fieldName, t), t);
    }
    return map;
  }

  public static Set<String> intersection(Set<String> left, Set<String> right) {
    Set result = new LinkedHashSet(left);
    result.retainAll(right);
    return result;
  }

  public static Set<String> differenceSet(Set<String> left, Set<String> right) {
    Set result = new LinkedHashSet(left);
    result.removeAll(right);
    return result;
  }

  public static Set<String> union(Set<String> left, Set<String> right) {
    Set<String> result = new LinkedHashSet();
    result.addAll(left);
    result.addAll(right);
    return result;
  }
}
