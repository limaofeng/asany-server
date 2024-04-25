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
package cn.asany.ui.library.service;

import cn.asany.ui.TestApplication;
import cn.asany.ui.library.domain.Library;
import cn.asany.ui.library.domain.enums.LibraryType;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class LibraryServiceTest {

  @Autowired private LibraryService libraryService;

  @Test
  void libraries() {
    List<Library> libraries =
        libraryService.libraries(PropertyFilter.newFilter(), LibraryType.ICONS, false);
    log.debug("libraries length " + libraries.size());
  }

  @Test
  void save() {}

  @Test
  void delete() {
    List<Library> libraries =
        libraryService.libraries(PropertyFilter.newFilter(), LibraryType.ICONS, false);
    for (Library library : libraries) {
      libraryService.delete(library.getId());
    }
  }
}
