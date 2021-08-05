package cn.asany.ui.library.service;

import cn.asany.ui.TestApplication;
import cn.asany.ui.library.bean.Library;
import cn.asany.ui.library.bean.enums.LibraryType;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class LibraryServiceTest {

    @Autowired
    private LibraryService libraryService;

    @Test
    void libraries() {
        List<Library> libraries = libraryService.libraries(PropertyFilter.builder(), LibraryType.ICONS, false);
        log.debug("libraries length " + libraries.size());
    }

    @Test
    void save() {
    }

    @Test
    void delete() {
        List<Library> libraries = libraryService.libraries(PropertyFilter.builder(), LibraryType.ICONS, false);
        for (Library library : libraries) {
            libraryService.delete(library.getId());
        }
    }
}