package cn.asany.sunrise.calendar.util;

import static java.util.regex.Pattern.compile;

import cn.asany.sunrise.calendar.domain.CalendarEvent;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import org.jfantasy.framework.dao.hibernate.util.ReflectionUtils;
import org.jfantasy.framework.util.common.StringUtil;

/**
 * Calendar Utils
 *
 * @author limaofeng
 */
public class CalendarUtils {

  private static final CalendarBuilder BUILDER = new CalendarBuilder();

  public static Calendar loadRemote(String url) throws IOException, ParserException {
    return BUILDER.build(new URL(url).openStream());
  }

  public static String getProperty(Calendar calendar, String... names) {
    for (String name : names) {
      Property property = calendar.getProperty(name);
      if (property == null) {
        continue;
      }
      return property.getValue();
    }
    return null;
  }

  private static boolean isAllDay(VEvent event) {
    if (null == event.getStartDate()) {
      return false;
    }
    ParameterList parameters = event.getStartDate().getParameters();
    Parameter parameter = parameters.getParameter("VALUE");
    return parameter != null && "DATE".equals(parameter.getValue());
  }

  private static String getValue(Property property) {
    if (property == null) {
      return null;
    }
    return property.getValue();
  }

  public static String randomColor() {
    Color color =
        Color.getHSBColor(
                new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat())
            .darker();
    return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
  }

  public static List<CalendarEvent> parseEvents(
      Calendar icalendar, cn.asany.sunrise.calendar.domain.Calendar calendar) {
    List<CalendarEvent> events = new ArrayList<>();
    for (Object value : icalendar.getComponents(Component.VEVENT)) {
      VEvent event = (VEvent) value;

      CalendarEvent.CalendarEventBuilder builder = CalendarEvent.builder();

      boolean allDay = isAllDay(event);

      Date starts = ReflectionUtils.convert(event.getStartDate().getValue(), Date.class);
      Date ends = ReflectionUtils.convert(event.getEndDate().getValue(), Date.class);

      builder
          .datetime(allDay, starts, ends)
          .title(event.getSummary().getValue())
          .notes(getValue(event.getDescription()))
          .location(getValue(event.getLocation()))
          .calendar(calendar);

      //      // ????????????
      //      if (null != event.getCreated()) {
      //        System.out.println("???????????????" + event.getCreated().getValue());
      //      }
      //      // ??????????????????
      //      if (null != event.getLastModified()) {
      //        System.out.println("?????????????????????" + event.getLastModified().getValue());
      //      }

      // ????????????
      if (null != event.getProperty("RRULE")) {
        System.out.println("RRULE:" + event.getProperty("RRULE").getValue());
      }
      // ??????????????????
      for (Object o : event.getAlarms()) {
        VAlarm alarm = (VAlarm) o;
        Pattern p = compile("[^0-9]");
        String aheadTime = alarm.getTrigger().getValue();
        Matcher m = p.matcher(aheadTime);
        int timeTemp = Integer.parseInt(m.replaceAll("").trim());
        if (aheadTime.endsWith("W")) {
          System.out.println("???????????????" + timeTemp + "???");
        } else if (aheadTime.endsWith("D")) {
          System.out.println("???????????????" + timeTemp + "???");
        } else if (aheadTime.endsWith("H")) {
          System.out.println("???????????????" + timeTemp + "??????");
        } else if (aheadTime.endsWith("M")) {
          System.out.println("???????????????" + timeTemp + "??????");
        } else if (aheadTime.endsWith("S")) {
          System.out.println("???????????????" + timeTemp + "???");
        }
      }
      // ?????????
      if (null != event.getProperty("ATTENDEE")) {
        ParameterList parameters = event.getProperty("ATTENDEE").getParameters();
        System.out.println(event.getProperty("ATTENDEE").getValue().split(":")[1]);
        System.out.println(parameters.getParameter("PARTSTAT").getValue());
      }
      events.add(builder.build());
    }
    return events;
  }

  public static String getColor(Calendar icalendar) {
    return StringUtil.defaultValue(
        CalendarUtils.getProperty(icalendar, "X-COLOR", "X-APPLE-CALENDAR-COLOR"),
        CalendarUtils.randomColor());
  }
}
