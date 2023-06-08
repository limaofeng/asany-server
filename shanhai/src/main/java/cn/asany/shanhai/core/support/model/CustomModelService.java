package cn.asany.shanhai.core.support.model;

import java.util.List;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.Page;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.util.common.StringUtil;

public interface CustomModelService {

  /**
   * 查询单个对象
   *
   * @param id ID
   * @param <T> 实体类型
   * @return T
   */
  <T> T get(Long id);

  /**
   * 查询多个对象
   *
   * @param filters 过滤条件
   * @param offset 开始位置
   * @param limit 数据条数
   * @param sort 排序
   * @param <T> 实体类型
   * @return 数据集
   */
  <T> List<T> findAll(PropertyFilter filter, int offset, int limit, OrderBy sort);

  /**
   * 分页查询
   *
   * @param page 分页对象
   * @param filters 过滤条件
   * @param <T> 实体类型
   * @return 分页结果
   */
  <T> Page<T> findPage(Page<T> page, PropertyFilter filter);

  /**
   * 保存对象
   *
   * @param entity 传入的对象
   * @param <T> 实体类型
   * @return 保存后的对象
   */
  <T> T save(T entity);

  /**
   * 更新对象
   *
   * @param id ID
   * @param entity 传入的对象
   * @param merge 是否合并
   * @param <T> 实体类型
   * @return 保存后的对象
   */
  <T> T update(Long id, T entity, boolean merge);

  /**
   * 删除单个对象
   *
   * @param id ID
   * @param <T> 实体类型
   * @return 返回被删除的对象
   */
  <T> T delete(Long id);

  /**
   * 删除多个对象
   *
   * @param filters 过滤条件
   * @param <T> 实体类型
   * @return 返回被删除的对象集合
   */
  <T> List<T> deleteMany(PropertyFilter filter);

  /**
   * 排序转换方法
   *
   * @param orderBy 排序枚举对象
   * @return 内部 OrderBy 对象
   */
  static OrderBy toOrderBy(Enum<?> orderBy) {
    if (orderBy == null) {
      return OrderBy.unsorted();
    }
    String[] stRs = StringUtil.tokenizeToStringArray(orderBy.name(), "_");
    return OrderBy.newOrderBy(stRs[0], OrderBy.Direction.valueOf(stRs[1]));
  }
}
