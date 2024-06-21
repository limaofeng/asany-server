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
