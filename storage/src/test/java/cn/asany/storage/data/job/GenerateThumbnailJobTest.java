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
package cn.asany.storage.data.job;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import cn.asany.storage.TestApplication;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.spring.SpringBeanUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class GenerateThumbnailJobTest {

  @Test
  void execute() {
    GenerateThumbnailJob generateThumbnailJob =
        SpringBeanUtils.createBean(
            GenerateThumbnailJob.class, SpringBeanUtils.AutoType.AUTOWIRE_BY_TYPE);
    JobExecutionContext context = mock(JobExecutionContext.class);
    // 创建数据映射
    JobDataMap jobDataMap = new JobDataMap();
    jobDataMap.put("source", "30680");
    jobDataMap.put("size", "512x512");

    // 模拟 context 返回 JobDataMap
    when(context.getMergedJobDataMap()).thenReturn(jobDataMap);

    generateThumbnailJob.execute(context);
  }
}
