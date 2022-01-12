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

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CalendarServiceTest {

  @Autowired private CalendarService calendarService;

  String url =
      "https://p30-calendars.icloud.com/published/2/yw_yM8prSymJqsGv4gmJP30ymsCx4FFL76qZBfZay2kyAG7KwlC869qtn3wfw2-b1-hfX89ecx6-tgLXf6SiHeE3cmY8hvHaWqgqmcHsXI4";

  //  String url = "https://calendars.icloud.com/holidays/cn_zh.ics";

  @Test
  void calendarSets() {
    calendarService.calendarSets(1L);
  }

  @Test
  void createCalendarSet() {
    calendarService.createCalendarSet();
  }

  @Test
  void updateCalendarSet() {
    calendarService.updateCalendarSet();
  }

  @Test
  void subscribe() {
    Calendar calendar = calendarService.subscribe(url);
    log.debug(String.format("events %d", calendar.getEvents().size()));
  }

  @Test
  void refresh() {
    Optional<Calendar> optional = calendarService.findByUrl(url);
    assert optional.isPresent();
    Calendar calendar = calendarService.refresh(optional.get().getId());
    log.debug(String.format("events %d", calendar.getEvents().size()));
  }

  @Test
  void delete() {
    Optional<Calendar> optional = calendarService.findByUrl(url);
    assert optional.isPresent();
    calendarService.delete(optional.get().getId());
  }
}
