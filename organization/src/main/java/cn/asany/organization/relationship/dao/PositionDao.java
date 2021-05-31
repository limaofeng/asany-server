package cn.asany.organization.relationship.dao;

import cn.asany.organization.core.bean.Job;
import cn.asany.organization.relationship.bean.Position;
import org.apache.ibatis.annotations.Param;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author limaofeng
 * @version V1.0
 * @Description: TODO
 * @date 2019-05-05 10:28
 */
@Repository
public interface PositionDao extends JpaRepository<Position, Long> {

    @Modifying
    @Query(nativeQuery = true, value = "delete from org_position where job_id=?1")
    void  deleteByJob(@Param("jobId") Long jobId);

    List<Position> findByJob(Job job);
}
