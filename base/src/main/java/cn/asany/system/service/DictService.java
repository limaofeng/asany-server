package cn.asany.system.service;

import cn.asany.system.dao.DictDao;
import cn.asany.system.dao.DictTypeDao;
import cn.asany.system.domain.Dict;
import cn.asany.system.domain.DictKey;
import cn.asany.system.domain.DictType;
import com.github.stuxuhai.jpinyin.PinyinException;
import java.util.*;
import java.util.stream.Collectors;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.framework.util.PinyinUtils;
import org.jfantasy.framework.util.common.BeanUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 字典服务
 *
 * @author limaofeng
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DictService {
  public static final String CACHE_KEY = "SYS_DICT";
  private final DictTypeDao dictTypeDao;
  private final DictDao dictDao;

  public DictService(DictTypeDao dictTypeDao, DictDao dictDao) {
    this.dictTypeDao = dictTypeDao;
    this.dictDao = dictDao;
  }

  // 查询数据字典类型列表
  public List<DictType> getDictTypes() {
    return dictTypeDao.findAll(Sort.by("path").ascending());
  }

  public Dict get(DictKey key) {
    return dictDao.findById(key).orElse(null);
  }

  public Optional<Dict> findByCode(String code, String type) {
    return dictDao.findById(DictKey.newInstance(code, type));
  }

  public Dict get(String key) {
    String[] array = key.split(":");
    return this.get(DictKey.newInstance(array[1], array[0]));
  }

  /**
   * 通过分类及上级编码查询配置项
   *
   * @param type 分类
   * @param parentCode 上级编码
   * @return {List}
   */
  public List<Dict> list(final String type, String parentCode) {
    if (parentCode != null) {
      return dictDao.findAll(
          (root, query, builder) ->
              builder.and(
                  builder.equal(root.get("type"), type),
                  builder.equal(root.get("parent").get("code"), parentCode)),
          Sort.by("sort").ascending());
    } else {
      return dictDao.findAll(
          (root, query, builder) -> builder.equal(root.get("type"), type),
          Sort.by("sort").ascending());
    }
  }

  /**
   * 分页查询方法
   *
   * @param pageable 分页对象
   * @param filter 过滤条件
   * @return {List}
   */
  public Page<Dict> findPage(Pageable pageable, PropertyFilter filter) {
    return this.dictDao.findPage(pageable, filter);
  }

  @Cacheable(key = "targetClass + methodName + '#' + #p0.toString()", value = CACHE_KEY)
  public List<Dict> findAll(PropertyFilter filter) {
    return this.dictDao.findAll(filter);
  }

  public List<Dict> findAll(PropertyFilter filter, Sort orderBy) {
    return this.dictDao.findAll(filter, orderBy);
  }

  public Page<DictType> findDictTypePage(Pageable pageable, PropertyFilter filter) {
    return this.dictTypeDao.findPage(pageable, filter);
  }

  public List<Dict> findType(String type) {
    return this.dictDao.findAll((root, query, builder) -> builder.equal(root.get("type"), type));
  }

  @CacheEvict(value = CACHE_KEY)
  public List<Dict> saveAll(List<Dict> dicts) {
    dicts =
        ObjectUtil.recursive(
            dicts,
            (item, context) -> {
              Dict parent = context.getParent();
              if (parent != null) {
                item.setPath(parent.getPath() + item.getCode() + Dict.PATH_SEPARATOR);
                item.setParentKey(parent.getId());
              } else {
                item.setPath(Dict.PATH_SEPARATOR + item.getCode() + Dict.PATH_SEPARATOR);
              }
              item.setLevel(context.getLevel());
              item.setIndex(context.getIndex());
              return item;
            });

    List<Dict> allDicts = ObjectUtil.flat(dicts, "children", "parent");

    this.dictDao.saveAllInBatch(allDicts);

    return allDicts;
  }

  public List<Dict> saveAll(List<Dict> dicts, String type) {
    Optional<DictType> typeOptional = this.dictTypeDao.findById(type);

    DictType dictType =
        typeOptional.orElseThrow(() -> new NotFoundException("字典类型[" + type + "]不存在"));

    dicts =
        ObjectUtil.recursive(
            dicts,
            (item, context) -> {
              Dict parent = context.getParent();
              if (parent != null) {
                item.setPath(parent.getPath() + item.getCode() + Dict.PATH_SEPARATOR);
                item.setParentKey(parent.getId());
              } else {
                item.setPath(Dict.PATH_SEPARATOR + item.getCode() + Dict.PATH_SEPARATOR);
              }
              item.setType(type);
              item.setLevel(context.getLevel());
              item.setIndex(context.getIndex());
              return item;
            });

    List<Dict> allDicts = ObjectUtil.flat(dicts, "children", "parent");

    this.dictDao.saveAllInBatch(allDicts);

    return allDicts;
  }

  /**
   * 添加及更新配置项
   *
   * @param dataDictionary 数据字典项
   */
  public Dict save(Dict dataDictionary) throws PinyinException {
    if (dataDictionary.getCode() == null) {
      dataDictionary.setCode(PinyinUtils.getAll(dataDictionary.getName()));
    }
    return this.dictDao.save(dataDictionary);
  }

  public Dict updates(String id, Dict dataDictionary) {
    List<Map<String, Object>> mapList = this.updateDict(id, dataDictionary);
    if (mapList.size() > 0) {
      for (Map map : mapList) {
        String code1 = String.valueOf(map.get("code"));
        String type1 = String.valueOf(map.get("type"));
        //        this.dictDao.updateDictPtype(
        //            dataDictionary.getType(), dataDictionary.getCode(), code1, type1);
      }
    }
    String ids = dataDictionary.getType() + ":" + dataDictionary.getCode();
    return this.dictDao.findById(DictKey.newInstance(ids)).orElse(null);
  }

  public List<Map<String, Object>> updateDict(String id, Dict dataDictionary) {
    DictKey dataDictionaryKey = DictKey.newInstance(id);
    String code = dataDictionaryKey.getCode();
    String type = dataDictionaryKey.getType();
    // 根据主键ID查询数据字典表中是否存在该条数据
    Dict allDict = dictDao.findById(DictKey.newInstance(id)).orElse(null);
    String ptype = null;
    String pcode = null;
    List<Map<String, Object>> mapList = new ArrayList<>();
    if (allDict != null) {
      // 如果存在，查询其关联的数据，并置空pcode和ptype
      //      mapList = this.dictDao.selectDict(code, type);
      if (mapList.size() > 0) {
        for (Map map : mapList) {
          String code1 = String.valueOf(map.get("code"));
          String type1 = String.valueOf(map.get("type"));
          //          this.dictDao.updateDictNull(code1, type1);
        }
      }
      // 置空后更新该条数据
      String types = dataDictionary.getType();
      String name = dataDictionary.getName();
      String description = dataDictionary.getDescription();
      Integer s = 0;
      if (dataDictionary.getParent() != null) {
        pcode = dataDictionary.getParent().getCode();
        ptype = dataDictionary.getParent().getType();
        //        s = this.dictDao.updateDict(types, name, description, pcode, ptype, code, type);
      } else {
        //        s = this.dictDao.updateDicts(types, name, description, code, type);
        // 然后再更新其关联的数据
      }
    }
    return mapList;
  }

  //  private List<DictT> initEntity(DictType dictType) {
  //    if (dictType.getParent() == null || StringUtil.isBlank(dictType.getParent().getId())) {
  //      dictType.setLevel(1);
  //      dictType.setPath(DictType.PATH_SEPARATOR + dictType.getCode() + DictType.PATH_SEPARATOR);
  //        dictType.set
  //      return dictDao.findAll(
  //          PropertyFilter.builder().isNull("parent").build(), Sort.by("sort").ascending());
  //    } else {
  //      DictType parentCategory =
  //          this.dictTypeDao.findById(dictType.getParent().getId()).orElse(null);
  //      assert parentCategory != null;
  //      dictType.setLevel(parentCategory.getLevel() + 1);
  //      dictType.setPath(
  //          parentCategory.getPath()
  //              + (dictTypeDao.findAll().size() + 1)
  //              + DictType.PATH_SEPARATOR); // 设置path
  //      String path =
  //          parentCategory.getPath() + (dictTypeDao.findAll().size() + 1) +
  // DictType.PATH_SEPARATOR;
  //      return dictDao.findAll(
  //          (root, query, builder) ->
  //              builder.equal(root.get("parent").get("code"), parentCategory.getId()),
  //          Sort.by("sort").ascending());
  //    }
  //  }

  /**
   * 添加及更新配置项分类方法
   *
   * @param dictType 数据字典分类
   */
  public DictType save(DictType dictType) {
    long index = this.dictTypeDao.count(PropertyFilter.newFilter().isNull("parent")) + 1;

    if (this.dictTypeDao.existsById(dictType.getId())) {
      throw new ValidationException("编码已经存在[" + dictType.getId() + "], 请使用新的编码重新保存");
    }

    dictType.setLevel(1);
    dictType.setPath(DictType.PATH_SEPARATOR + dictType.getId() + DictType.PATH_SEPARATOR);
    dictType.setIndex((int) index);

    if (dictType.getParent() != null) {
      Optional<DictType> optionalParent = this.dictTypeDao.findById(dictType.getParent().getId());
      DictType parent =
          optionalParent.orElseThrow(
              () -> new NotFoundException("错误的上级编码[" + dictType.getParent().getId() + "]"));
      PropertyFilter filter = PropertyFilter.newFilter().equal("parent.id", parent.getId());
      index = this.dictTypeDao.count(filter) + 1;

      dictType.setLevel(parent.getLevel() + 1);
      dictType.setPath(parent.getPath() + dictType.getId() + DictType.PATH_SEPARATOR);
      dictType.setIndex((int) index);
    }

    return this.dictTypeDao.save(dictType);
  }

  public DictType update(String id, DictType dictType) {
    List<DictType> types;
    boolean root = false;
    if (dictType.getParent() == null || StringUtil.isBlank(dictType.getParent().getId())) {
      dictType.setLevel(0);
      dictType.setPath((dictTypeDao.findAll().size() + 1) + DictType.PATH_SEPARATOR);
      root = true;
      types =
          dictTypeDao.findAll(
              (roots, query, builder) -> builder.isNull(roots.get("parent")),
              Sort.by("sort").ascending());
      /* types = ObjectUtil.sort(dictTypeDao.find(Restrictions.isNull("parent")), "sort", "asc");*/
    } else {
      DictType parentCategory =
          this.dictTypeDao.findById(dictType.getParent().getId()).orElse(null);
      dictType.setLevel(parentCategory.getLevel() + 1);
      dictType.setPath(
          parentCategory.getPath()
              + (dictTypeDao.findAll().size() + 1)
              + DictType.PATH_SEPARATOR); // 设置path
      types =
          dictTypeDao.findAll(
              (roots, query, builder) ->
                  builder.equal(roots.get("parent").get("id"), parentCategory.getId()),
              Sort.by("sort").ascending());
      /*types = ObjectUtil.sort(dictTypeDao.findBy("parent.code", parentCategory.getId()), "sort", "asc");*/
    }
    DictType old = this.dictTypeDao.findById(id).orElse(null);
    final DictType olds = old;
    if (dictType.getIndex() != null
        && (ObjectUtil.find(types, "code", old.getId()) == null
            || !old.getIndex().equals(dictType.getIndex()))) {
      if (ObjectUtil.find(types, "code", old.getId()) == null) { // 移动了节点的层级
        int i = 0;
        for (DictType m :
            old.getParent() == null
                ? dictTypeDao.findAll(
                    (roots, query, builder) -> builder.isNull(roots.get("parent")),
                    Sort.by("sort").ascending())
                : dictTypeDao.findAll(
                    (roots, query, builder) ->
                        builder.equal(roots.get("parent").get("id"), olds.getParent().getId()),
                    Sort.by("sort").ascending())) {
          m.setIndex(i++);
          m.setId(id);
          this.dictTypeDao.save(m);
        }
        types.add(dictType.getIndex() - 1, dictType);
      } else {
        DictType t = ObjectUtil.remove(types, "code", old.getId());
        if (types.size() >= dictType.getIndex()) {
          types.add(dictType.getIndex() - 1, t);
        } else {
          types.add(t);
        }
      }
      // 重新排序后更新新的位置
      for (int i = 0; i < types.size(); i++) {
        DictType m = types.get(i);
        if (m.getId().equals(dictType.getId())) {
          continue;
        }
        m.setIndex(i + 1);
        m.setId(id);
        this.dictTypeDao.save(m);
      }
    }
    old = BeanUtil.copyProperties(old, dictType, "children", "id");
    if (root) {
      old.setParent(null);
    }
    old.setId(id);
    return this.dictTypeDao.save(old);
  }

  /**
   * 删除配置项
   *
   * @param keys 数据字段 key
   */
  public void delete(DictKey... keys) {
    for (DictKey key : keys) {
      Dict dd = this.get(key);
      if (dd == null) {
        continue;
      }
      this.dictDao.delete(dd);
    }
  }

  public void delete(String... keys) {
    List<DictKey> dictKeys = new ArrayList<>();
    for (String key : keys) {
      dictKeys.add(new DictKey(key));
    }
    this.delete(dictKeys.toArray(new DictKey[0]));
  }

  public int deleteDictByType(String type) {
    return this.dictDao.deleteDictByType(type);
  }

  public void deleteType(String... codes) {
    List<DictType> types =
        Arrays.stream(codes).map(this.dictTypeDao::getReferenceById).collect(Collectors.toList());
    for (DictType type : types) {
      this.dictDao.deleteAllInBatch(type.getDicts());
    }
    this.dictTypeDao.deleteAllInBatch(types);
  }

  /** 删除字典 */
  public void deleteById(String id) {
    dictDao.deleteById(DictKey.newInstance(id));
  }

  public DictType getType(String id) {
    return dictTypeDao.findById(id).orElse(null);
  }

  /** 根据数据字典id，查询数据字典 */
  public Dict dataDictionary(String id) {
    return dictDao.findById(DictKey.newInstance(id)).orElse(null);
  }

  public List<DictType> findTypes(Example<DictType> partyQueryMenu) {
    return dictTypeDao.findAll(partyQueryMenu);
  }

  public Optional<Dict> findOne(PropertyFilter filter) {
    return this.dictDao.findOne(filter);
  }
}
