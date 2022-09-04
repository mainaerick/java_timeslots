import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    String day = "2014-11-25";

    String[] start_time = { "07:00", "11:00", "14:00", "16:30" };
    String[] totime = { "09:00", "13:00", "16:00", "18:00" };

    DateTimeFormatter formatter;
    LocalDateTime localDateTime_day_to;
    LocalDateTime localDateTime_day_from;
    Duration day_diff = null;
    long diff_mili = 0;
    String day_diff_string = null;
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    DateFormat format2 = new SimpleDateFormat("hh:mm a");
    List<Date> intervals = new ArrayList<>(25);

    public static void main(String[] args) throws ParseException {


    }
    private void Input(){
        System.out.println("Enter Date (e.g. 2022-09-01)");
        String date_to_get_timeslots  = new Scanner(System.in).nextLine();

        System.out.println("What time does te day begins?  (e.g. 06:00)");

        String time_day_begins  = new Scanner(System.in).nextLine();
        System.out.println("What time does te day end?  (e.g. 17:00)");

        String time_day_ends  = new Scanner(System.in).nextLine();

    }
    private void Intervals(){
        String day_from = "2014-11-25 06:00", day_to = "2014-11-25 20:00";// the start of the day to the end of the day


        Date start_time_day = format.parse(day_from);// convaert day start to date
        Date end_time_day = format.parse(day_to); // convert dya end to date
        Date first_task_date = format.parse(day + start_time[0]);

        String first_task_date_string = format2.format(first_task_date); // convert date day start to string
        String setd2 = format.format(end_time_day); // convert date day end to string

        // Date dstd2=format2.parse(sstd2);
        // Date detd2=format2.parse(setd2);

        intervals.add(start_time_day); // add date start day to list intervals
    }

    private void Compute(Date start_time_day,Date end_time_day){
        Date first_task_date = format.parse(day + start_time[0]);

        String first_task_date_string = format2.format(first_task_date); // convert date day start to string
        String setd2 = format.format(end_time_day); // convert date day end to string

        Calendar cal = Calendar.getInstance(), cal2 = Calendar.getInstance();
        cal.setTime(start_time_day);
        Date first_task_time = format.parse(day + start_time[0]);
        cal2.setTime(first_task_time);

        // if day start ie 7:00 am is less than first task time 8:00, have a free time
        // slot of one hour from 7-8
        if (cal.before(cal2)) {
            String first_task_time_string = format2.format(start_time_day);

            System.out.println("free time slots " + first_task_time_string + " - " + first_task_date_string);
        }

        while (cal.getTime().before(end_time_day)) { // loop through every minute from day start time to day end time
            // and add it to the list intervals
            int minute = cal.get(Calendar.MINUTE);
            cal.add(Calendar.MINUTE, 1);
            intervals.add(cal.getTime());
        }

        for (int i = 0; i < start_time.length; i++) {

            Date date2 = format.parse(day + totime[i]);

            String totime_string = format2.format(date2);

            for (int j = 0; j < intervals.size(); j++) {

                if (format2.format(intervals.get(j)).equals(totime_string)) {
                    Date date;
                    if (i + 1 == start_time.length) {
                        date = format.parse(setd2); // if its last time in the array set the date to end day time

                    } else {

                        date = format.parse(day + start_time[i + 1]); // else set date to next task start time
                    }

                    String time_string = format2.format(date);

                    System.out.println("free time slots " + totime_string + " - " + time_string);
                }

            }

            // --------------------------------------------------------------------------------------------------------------------------------
            // calculate duration between day start and day end time
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            localDateTime_day_from = LocalDateTime.parse(day_from, formatter); // whole day format from string
            localDateTime_day_to = LocalDateTime.parse(day_to, formatter); // whole day format from string

            day_diff = Duration.between(localDateTime_day_from, localDateTime_day_to); // duration for the whole day

            day_diff_string = String.format("%d hour(s) %02d minutes", day_diff.toHoursPart(),
                    day_diff.toMinutesPart()); // whole day duration format to string

            try {

                LocalDateTime lDateTime1 = LocalDateTime.parse(day + start_time[i], formatter); // from time
                // localdatetime
                LocalDateTime lDateTime2 = LocalDateTime.parse(day + totime[i], formatter); // to time local date time

                Duration diff = Duration.between(lDateTime1, lDateTime2); // duration between task from time and task to
                // time
                long temp = diff.toMillis(); // temporary value to hold diff converted to milliseconds
                diff_mili = temp + diff_mili; // sum of all tasks duration for the day

                String format_duration_string = String.format("%d hour(s) %02d minutes", diff.toHoursPart(),
                        diff.toMinutesPart());

                // System.out.println("duration of time " + i + " in hours and minute is " +
                // format_duration_string);

            } catch (Exception e) {
                // TODO: handle exception
            }
            // ----------------------------------------------------------------------------------------------------------------------------------

        }

        Duration ddmili_duration = Duration.ofMillis(diff_mili); // milliseconds to duration
        String format_duration_string = String.format("%d hour(s) %02d minutes", ddmili_duration.toHoursPart(),
                ddmili_duration.toMinutesPart());
        System.out.println("sum of time taken by tasks is " + format_duration_string);

        long day_diff_mili = day_diff.toMillis() - ddmili_duration.toMillis();
        Duration freetime_duration = Duration.ofMillis(day_diff_mili);
        String free_duration_string = String.format("%d hour(s) %02d minutes", freetime_duration.toHoursPart(),
                freetime_duration.toMinutesPart());
        System.out.println("whole day duration is " + day_diff_string + " free time is " + free_duration_string); // output
    }
}
