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
package cn.asany.sunrise.calendar.dao;

import cn.asany.sunrise.calendar.domain.CalendarSet;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 日历集
 *
 * @author limaofeng
 */
@Repository
public interface CalendarSetDao extends AnyJpaRepository<CalendarSet, Long> {
  @Query(value = "SELECT max(index) FROM CalendarSet where owner.id = :uid")
  Integer getMaxIndex(@Param("uid") Long uid);
}
