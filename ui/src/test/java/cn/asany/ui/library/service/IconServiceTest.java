package cn.asany.ui.library.service;

import cn.asany.ui.TestApplication;
import cn.asany.ui.resources.domain.Icon;
import cn.asany.ui.resources.service.IconService;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
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
class IconServiceTest {

  @Autowired private IconService iconService;

  @Test
  void findAllByTag() {
    Set<Icon> icons = iconService.findAllByTag(1L, "地图");
    log.debug(String.format("map icons = %d", icons.size()));
    print(icons);

    icons = iconService.findAllByTag(1L, "地图/美洲");
    log.debug(String.format("map icons = %d", icons.size()));
    print(icons);
  }

  void print(Set<Icon> icons) {
    icons.stream()
        .forEach(
            item -> {
              log.debug(
                  String.format(
                      "icon name = %s, unicode = %s, content = %s",
                      item.getName(), item.getUnicode(), item.getContent()));
            });
  }
}
