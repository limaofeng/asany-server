package cn.asany.flowable.core.dao;

import cn.asany.flowable.core.domain.ProcessModel;
import cn.asany.flowable.core.graphql.input.ProcessModelFilter;
import org.jfantasy.framework.dao.Page;
import org.jfantasy.framework.dao.mybatis.sqlmapper.SqlMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessModelDao extends SqlMapper {

  ProcessModel getProcessModel(String id);

  Page<ProcessModel> findPage(Page page, ProcessModelFilter filter);
}
