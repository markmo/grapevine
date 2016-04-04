package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by markmo on 5/04/2016.
 */
public class StringToTime {

    private static final List<DateMatcher> dateMatchers;

    private static final String[] daysOfWeek = new String[]{
            "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"
    };

    // See https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
    static {
        dateMatchers = new LinkedList<DateMatcher>();
        dateMatchers.add(new DayOfWeekMatcher());
        dateMatchers.add(new DateFormatMatcher(new SimpleDateFormat("yyyy-MMM-dd, HH:mm a")));
        dateMatchers.add(new NowMatcher());
        dateMatchers.add(new TomorrowMatcher());
        dateMatchers.add(new DaysMatcher());
        dateMatchers.add(new DateFormatMatcher(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
        dateMatchers.add(new DateFormatMatcher(new SimpleDateFormat("dd/MM/yyyy")));
    }

    private StringToTime() {
        throw new UnsupportedOperationException();
    }

    public static void registerMatcher(DateMatcher dateMatcher) {
        dateMatchers.add(dateMatcher);
    }

    public interface DateMatcher {

        Date tryConvert(String input);

    }

    private static class DateFormatMatcher implements DateMatcher {

        private final DateFormat dateFormat;

        public DateFormatMatcher(DateFormat dateFormat) {
            this.dateFormat = dateFormat;
        }

        public Date tryConvert(String input) {
            try {
                return dateFormat.parse(input);
            } catch (ParseException e) {
                return null;
            }
        }

    }

    private static class NowMatcher implements DateMatcher {

        private final Pattern now = Pattern.compile("now");

        public Date tryConvert(String input) {
            if (now.matcher(input).matches()) {
                return new Date();
            }
            return null;
        }

    }

    private static class TomorrowMatcher implements DateMatcher {

        private final Pattern tomorrow = Pattern.compile("tomorrow");

        public Date tryConvert(String input) {
            if (tomorrow.matcher(input).matches()) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                return calendar.getTime();
            }
            return null;
        }

    }

    private static class DaysMatcher implements DateMatcher {

        private final Pattern days = Pattern.compile("([\\-\\+]?\\d+) days");

        public Date tryConvert(String input) {
            Matcher matcher = days.matcher(input);
            if (matcher.matches()) {
                int d = Integer.parseInt(matcher.group(1));
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, d);
                return calendar.getTime();
            }
            return null;
        }

    }

    private static class DayOfWeekMatcher implements DateMatcher {

        private final Pattern days = Pattern.compile("(monday|tuesday|wednesday|thursday|friday|saturday|sunday) at (\\d+):(\\d+) (am|pm)");

        public Date tryConvert(String input) {
            Matcher matcher = days.matcher(input);
            if (matcher.matches()) {
                String dayOfWeek = matcher.group(1);
                int hour = Integer.parseInt(matcher.group(2));
                int minutes = Integer.parseInt(matcher.group(3));
                String am_pm = matcher.group(4);
                if ("pm".equals(am_pm)) {
                    hour += 12;
                }
                Calendar calendar = Calendar.getInstance();
                int today = calendar.get(Calendar.DAY_OF_WEEK);
                int day = Arrays.binarySearch(daysOfWeek, dayOfWeek) + 1;
                int diff = today - day;
                int move = diff;
                if (diff < 0) {
                    move = -7 - diff;
                }
                calendar.add(Calendar.DAY_OF_YEAR, move);
                calendar.set(Calendar.HOUR, hour);
                calendar.set(Calendar.MINUTE, minutes);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                return calendar.getTime();
            }
            return null;
        }

    }

    public static Date apply(String input) {
        if (input == null) return null;
        String in = input.trim().toLowerCase();
        for (DateMatcher dateMatcher : dateMatchers) {
            Date date = dateMatcher.tryConvert(in);
            if (date != null) {
                return date;
            }
        }
        return null;
    }

}
