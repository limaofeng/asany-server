package cn.asany.flowable.core.dao;

import cn.asany.flowable.core.domain.FormModel;
import org.jfantasy.framework.dao.Page;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.mybatis.sqlmapper.SqlMapper;
import org.springframework.stereotype.Repository;

/**
 * 表单模型
 *
 * @author limaofeng
 */
@Repository
public interface FormModelDao extends SqlMapper {

  /**
   * 查询表单模型
   *
   * @param id ID
   * @return ProcessModel
   */
  FormModel getFormModel(String id);

  /**
   * 查询表单模型
   *
   * @param page<ProcessModel> 分页信息
   * @param filter 过滤条件
   * @return Page<ProcessModel>
   */
  Page<FormModel> findPage(Page<FormModel> page, PropertyFilter filter);
}
