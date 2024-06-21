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
import net.asany.jfantasy.framework.dao.hibernate.util.ReflectionUtils;
import net.asany.jfantasy.framework.util.common.StringUtil;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;

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

      //      // 创建时间
      //      if (null != event.getCreated()) {
      //        System.out.println("创建时间：" + event.getCreated().getValue());
      //      }
      //      // 最后修改时间
      //      if (null != event.getLastModified()) {
      //        System.out.println("最后修改时间：" + event.getLastModified().getValue());
      //      }

      // 重复规则
      if (null != event.getProperty("RRULE")) {
        System.out.println("RRULE:" + event.getProperty("RRULE").getValue());
      }
      // 提前多久提醒
      for (Object o : event.getAlarms()) {
        VAlarm alarm = (VAlarm) o;
        Pattern p = compile("[^0-9]");
        String aheadTime = alarm.getTrigger().getValue();
        Matcher m = p.matcher(aheadTime);
        int timeTemp = Integer.parseInt(m.replaceAll("").trim());
        if (aheadTime.endsWith("W")) {
          System.out.println("提前多久：" + timeTemp + "周");
        } else if (aheadTime.endsWith("D")) {
          System.out.println("提前多久：" + timeTemp + "天");
        } else if (aheadTime.endsWith("H")) {
          System.out.println("提前多久：" + timeTemp + "小时");
        } else if (aheadTime.endsWith("M")) {
          System.out.println("提前多久：" + timeTemp + "分钟");
        } else if (aheadTime.endsWith("S")) {
          System.out.println("提前多久：" + timeTemp + "秒");
        }
      }
      // 邀请人
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
