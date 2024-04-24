package cn.asany.cms.learn.dao;

import cn.asany.cms.article.domain.Article;
import cn.asany.cms.learn.domain.Course;
import cn.asany.cms.learn.domain.Lesson;
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonDao extends AnyJpaRepository<Lesson, Long> {

  /**
   * 查询该课程下所有章节
   *
   * @param course
   * @return
   */
  List<Lesson> findByCourse(Course course);

  /**
   * 根据章节内容查询章节
   *
   * @param article
   * @return
   */
  Lesson findByArticle(Article article);
}
