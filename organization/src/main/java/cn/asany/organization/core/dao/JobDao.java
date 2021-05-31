package cn.asany.organization.core.dao;

import cn.asany.organization.core.bean.Job;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng
 * @version V1.0
 * @Description: TODO
 * @date 2019-05-10 09:04
 */
@Repository
public interface JobDao extends JpaRepository<Job, Long> {

}
