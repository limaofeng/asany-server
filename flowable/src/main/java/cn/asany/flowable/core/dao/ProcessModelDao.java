package cn.asany.flowable.core.dao;

import cn.asany.flowable.core.domain.ProcessModel;
import org.jfantasy.framework.dao.Page;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.mybatis.sqlmapper.SqlMapper;
import org.springframework.stereotype.Repository;

/**
 * 流程模型
 *
 * @author limaofeng
 */
@Repository
public interface ProcessModelDao extends SqlMapper {

  /**
   * 查询流程模型
   *
   * @param id ID
   * @return ProcessModel
   */
  ProcessModel getProcessModel(String id);

  /**
   * 查询流程模型
   *
   * @param page<ProcessModel> 分页信息
   * @param filter 过滤条件
   * @return Page<ProcessModel>
   */
  Page<ProcessModel> findPage(Page<ProcessModel> page, PropertyFilter filter);
}
