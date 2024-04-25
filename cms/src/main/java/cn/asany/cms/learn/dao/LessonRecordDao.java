/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.cms.learn.dao;

import cn.asany.cms.learn.domain.Course;
import cn.asany.cms.learn.domain.Learner;
import cn.asany.cms.learn.domain.Lesson;
import cn.asany.cms.learn.domain.LessonRecord;
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRecordDao extends AnyJpaRepository<LessonRecord, Long> {

  /**
   * 查询某用户的所有学习记录
   *
   * @param learner
   * @return
   */
  List<LessonRecord> findByLearner(Learner learner);

  /**
   * 查询某个人学习的课程下所有的学习记录
   *
   * @param lesson
   * @param learner
   * @param course
   * @return
   */
  LessonRecord findByLessonAndLearnerAndCourse(Lesson lesson, Learner learner, Course course);

  /**
   * 查询某个人对应某个课程的学习记录
   *
   * @param course
   * @param learner
   * @return
   */
  List<LessonRecord> findByCourseAndLearner(Course course, Learner learner);

  /**
   * 根据章节查询此章节下所有的学习记录
   *
   * @param lesson
   * @return
   */
  List<LessonRecord> findByLesson(Lesson lesson);

  /**
   * 根据学习人和章节查询学习记录
   *
   * @param learner
   * @param lesson
   * @return
   */
  LessonRecord findByLearnerAndLesson(Learner learner, Lesson lesson);
}
