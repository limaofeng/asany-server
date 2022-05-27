package cn.asany.cms.learn.dao;

import cn.asany.cms.learn.domain.CourseSection;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseSectionDao extends JpaRepository<CourseSection, Long> {}
