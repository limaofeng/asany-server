package cn.asany.sunrise.calendar.service;

import cn.asany.sunrise.TestApplication;
import cn.asany.sunrise.calendar.bean.Calendar;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CalendarServiceTest {

  @Autowired private CalendarService calendarService;

  String url =
      "https://raw.githubusercontent.com/infinet/lunar-calendar/master/chinese_lunar_prev_year_next_year.ics";

  @Test
  void subscribe() {
    Calendar calendar = calendarService.subscribe(url);
    log.debug(String.format("events %d", calendar.getEvents().size()));
  }

  @Test
  @Transactional
  void refresh() {
    Optional<Calendar> optional = calendarService.findByUrl(url);
    assert optional.isPresent();
    Calendar calendar = calendarService.refresh(optional.get().getId());
    log.debug(String.format("events %d", calendar.getEvents().size()));
  }
}
