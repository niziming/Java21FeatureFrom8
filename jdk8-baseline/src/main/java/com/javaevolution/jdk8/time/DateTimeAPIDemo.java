package com.javaevolution.jdk8.time;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

/**
 * Date-Time API 完整演示
 * JSR 310: Date and Time API
 */
public class DateTimeAPIDemo {

    public static class LocalDateTimeDemo {
        public void localDateDemo() {
            // 创建 LocalDate
            LocalDate today = LocalDate.now();
            LocalDate specificDate = LocalDate.of(2024, 1, 1);
            LocalDate parsed = LocalDate.parse("2024-12-31");
            
            System.out.println("Today: " + today);
            
            // 日期操作
            LocalDate tomorrow = today.plusDays(1);
            LocalDate nextWeek = today.plusWeeks(1);
            LocalDate nextMonth = today.plusMonths(1);
            LocalDate nextYear = today.plusYears(1);
            
            // 获取日期信息
            int year = today.getYear();
            Month month = today.getMonth();
            int dayOfMonth = today.getDayOfMonth();
            DayOfWeek dayOfWeek = today.getDayOfWeek();
            
            // 日期比较
            boolean isBefore = today.isBefore(tomorrow);
            boolean isAfter = today.isAfter(specificDate);
        }
        
        public void localTimeDemo() {
            // 创建 LocalTime
            LocalTime now = LocalTime.now();
            LocalTime specificTime = LocalTime.of(14, 30, 0);
            LocalTime parsed = LocalTime.parse("14:30:00");
            
            // 时间操作
            LocalTime later = now.plusHours(2);
            LocalTime earlier = now.minusMinutes(30);
            
            // 获取时间信息
            int hour = now.getHour();
            int minute = now.getMinute();
            int second = now.getSecond();
        }
        
        public void localDateTimeDemo() {
            // 创建 LocalDateTime
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime specific = LocalDateTime.of(2024, 1, 1, 14, 30);
            
            // 组合 LocalDate 和 LocalTime
            LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.of(14, 30);
            LocalDateTime combined = LocalDateTime.of(date, time);
            
            // 复杂操作
            LocalDateTime future = now
                .plusDays(1)
                .plusHours(2)
                .withMinute(0)
                .withSecond(0);
        }
    }

    public static class InstantAndZonedDateTimeDemo {
        public void instantDemo() {
            // Instant: 时间戳 (UTC)
            Instant now = Instant.now();
            Instant epoch = Instant.EPOCH; // 1970-01-01T00:00:00Z
            Instant specific = Instant.ofEpochMilli(1609459200000L);
            
            System.out.println("Current timestamp: " + now);
            System.out.println("Epoch millis: " + now.toEpochMilli());
            
            // Instant 操作
            Instant later = now.plus(Duration.ofHours(2));
            Instant earlier = now.minus(Duration.ofMinutes(30));
        }
        
        public void zonedDateTimeDemo() {
            // ZonedDateTime: 带时区的日期时间
            ZonedDateTime now = ZonedDateTime.now();
            ZonedDateTime tokyo = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
            ZonedDateTime newYork = ZonedDateTime.now(ZoneId.of("America/New_York"));
            
            System.out.println("Current: " + now);
            System.out.println("Tokyo: " + tokyo);
            System.out.println("New York: " + newYork);
            
            // LocalDateTime 转 ZonedDateTime
            LocalDateTime ldt = LocalDateTime.now();
            ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
            
            // 时区转换
            ZonedDateTime shanghai = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
            ZonedDateTime london = shanghai.withZoneSameInstant(ZoneId.of("Europe/London"));
            
            System.out.println("Shanghai: " + shanghai);
            System.out.println("London: " + london);
        }
        
        // Instant vs LocalDateTime 的区别
        public void instantVsLocalDateTime() {
            // LocalDateTime: 无时区概念,描述"某个时间"
            LocalDateTime ldt = LocalDateTime.of(2024, 1, 1, 0, 0);
            System.out.println("LocalDateTime: " + ldt);
            
            // Instant: 时间线上的绝对时刻 (UTC)
            Instant instant = Instant.parse("2024-01-01T00:00:00Z");
            System.out.println("Instant: " + instant);
            
            // 转换
            LocalDateTime fromInstant = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            Instant fromLdt = ldt.toInstant(ZoneOffset.UTC);
        }
    }

    public static class PeriodAndDurationDemo {
        public void durationDemo() {
            // Duration: 基于时间的量 (秒、纳秒)
            Duration oneHour = Duration.ofHours(1);
            Duration twoMinutes = Duration.ofMinutes(2);
            Duration fiveSeconds = Duration.ofSeconds(5);
            
            // Duration 计算
            LocalTime start = LocalTime.of(9, 0);
            LocalTime end = LocalTime.of(17, 30);
            Duration workHours = Duration.between(start, end);
            
            System.out.println("Work hours: " + workHours.toHours() + " hours");
            System.out.println("Work minutes: " + workHours.toMinutes() + " minutes");
            
            // Duration 操作
            Duration combined = oneHour.plus(twoMinutes);
            Duration multiplied = oneHour.multipliedBy(3);
        }
        
        public void periodDemo() {
            // Period: 基于日期的量 (年、月、日)
            Period oneMonth = Period.ofMonths(1);
            Period twoYears = Period.ofYears(2);
            Period tenDays = Period.ofDays(10);
            
            // Period 计算
            LocalDate start = LocalDate.of(2020, 1, 1);
            LocalDate end = LocalDate.of(2024, 6, 15);
            Period period = Period.between(start, end);
            
            System.out.println("Years: " + period.getYears());
            System.out.println("Months: " + period.getMonths());
            System.out.println("Days: " + period.getDays());
            
            // 使用 Period
            LocalDate birthday = LocalDate.of(1990, 5, 20);
            LocalDate today = LocalDate.now();
            Period age = Period.between(birthday, today);
            System.out.println("Age: " + age.getYears() + " years");
        }
        
        // Duration vs Period
        public void durationVsPeriod() {
            // Duration: 用于 LocalTime, LocalDateTime, Instant
            LocalDateTime start = LocalDateTime.of(2024, 1, 1, 9, 0);
            LocalDateTime end = start.plus(Duration.ofHours(8));
            
            // Period: 用于 LocalDate
            LocalDate startDate = LocalDate.of(2024, 1, 1);
            LocalDate endDate = startDate.plus(Period.ofMonths(3));
            
            System.out.println("End time: " + end);
            System.out.println("End date: " + endDate);
        }
    }

    public static class FormattingAndParsing {
        public void formattingDemo() {
            LocalDateTime now = LocalDateTime.now();
            
            // 预定义格式
            String iso = now.format(DateTimeFormatter.ISO_DATE_TIME);
            String basic = now.format(DateTimeFormatter.BASIC_ISO_DATE);
            
            // 自定义格式
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formatted1 = now.format(formatter1);
            
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");
            String formatted2 = now.format(formatter2);
            
            System.out.println("ISO: " + iso);
            System.out.println("Custom: " + formatted1);
            System.out.println("Chinese: " + formatted2);
        }
        
        public void parsingDemo() {
            // 解析日期时间
            String dateStr = "2024-12-31";
            LocalDate date = LocalDate.parse(dateStr);
            
            String dateTimeStr = "2024-12-31 23:59:59";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
            
            System.out.println("Parsed date: " + date);
            System.out.println("Parsed datetime: " + dateTime);
        }
    }

    public static class TemporalAdjustersDemo {
        public void temporalAdjustersDemo() {
            LocalDate date = LocalDate.now();
            
            // 下一个周一
            LocalDate nextMonday = date.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            
            // 本月第一天
            LocalDate firstDayOfMonth = date.with(TemporalAdjusters.firstDayOfMonth());
            
            // 本月最后一天
            LocalDate lastDayOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());
            
            // 下个月第一天
            LocalDate firstDayOfNextMonth = date.with(TemporalAdjusters.firstDayOfNextMonth());
            
            // 本年第一天
            LocalDate firstDayOfYear = date.with(TemporalAdjusters.firstDayOfYear());
            
            System.out.println("Next Monday: " + nextMonday);
            System.out.println("First day of month: " + firstDayOfMonth);
            System.out.println("Last day of month: " + lastDayOfMonth);
        }
        
        // 自定义 TemporalAdjuster
        public void customAdjuster() {
            LocalDate date = LocalDate.now();
            
            // 下一个工作日
            LocalDate nextWorkday = date.with(temporal -> {
                LocalDate result = LocalDate.from(temporal);
                DayOfWeek dow = result.getDayOfWeek();
                int daysToAdd = 1;
                if (dow == DayOfWeek.FRIDAY) daysToAdd = 3;
                else if (dow == DayOfWeek.SATURDAY) daysToAdd = 2;
                return result.plusDays(daysToAdd);
            });
            
            System.out.println("Next workday: " + nextWorkday);
        }
    }

    public static class TimeZoneHandling {
        public void timeZoneDemo() {
            // 获取所有时区
            java.util.Set<String> allZones = ZoneId.getAvailableZoneIds();
            System.out.println("Total zones: " + allZones.size());
            
            // 常用时区
            ZoneId shanghai = ZoneId.of("Asia/Shanghai");
            ZoneId tokyo = ZoneId.of("Asia/Tokyo");
            ZoneId newYork = ZoneId.of("America/New_York");
            ZoneId london = ZoneId.of("Europe/London");
            
            // 时区偏移
            ZoneOffset offset = ZoneOffset.of("+08:00");
            OffsetDateTime odt = OffsetDateTime.now(offset);
            
            // 夏令时处理
            ZonedDateTime summer = ZonedDateTime.of(2024, 7, 1, 12, 0, 0, 0, newYork);
            ZonedDateTime winter = ZonedDateTime.of(2024, 1, 1, 12, 0, 0, 0, newYork);
            
            System.out.println("Summer offset: " + summer.getOffset());
            System.out.println("Winter offset: " + winter.getOffset());
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Date-Time API Demo ===");
        
        PeriodAndDurationDemo pdd = new PeriodAndDurationDemo();
        pdd.durationDemo();
        pdd.periodDemo();
        
        TemporalAdjustersDemo tad = new TemporalAdjustersDemo();
        tad.temporalAdjustersDemo();
    }
}
