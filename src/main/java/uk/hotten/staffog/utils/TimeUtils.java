package uk.hotten.staffog.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class TimeUtils {

    public static DateFormat notificationTimeFormat = new SimpleDateFormat("dd-MM-yy");

    public static String formatMillisecondTime(long millis) {
        if (millis == -1)
            return "permanent";

        //create the list
        List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
        Collections.reverse(units);
        units.remove(TimeUnit.MILLISECONDS);
        units.remove(TimeUnit.MICROSECONDS);
        units.remove(TimeUnit.NANOSECONDS);

        //create the result map of TimeUnit and difference
        Map<TimeUnit,Long> result = new LinkedHashMap<TimeUnit,Long>();
        long milliesRest = millis;
        StringBuilder toReturn = new StringBuilder();

        for (TimeUnit unit : units) {

            //calculate difference in millisecond
            long diff = unit.convert(milliesRest,TimeUnit.MILLISECONDS);
            long diffInMilliesForUnit = unit.toMillis(diff);
            milliesRest = milliesRest - diffInMilliesForUnit;

            //put the result in the map
            result.put(unit, diff);
        }


        if (millis > 86400000) {
            result.remove(TimeUnit.MINUTES);
            result.remove(TimeUnit.SECONDS);
        }

        for (Map.Entry<TimeUnit, Long> entry : result.entrySet()) {
            if (entry.getValue() == 0)
                continue;

            toReturn.append(entry.getValue()).append(" ").append(entry.getKey().name().toLowerCase()).append(" ");
        }

        return toReturn.toString().trim();
    }

}
