package cn.asany.cms.learn.dao;

import cn.asany.cms.learn.domain.CourseSection;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseSectionDao extends AnyJpaRepository<CourseSection, Long> {}
