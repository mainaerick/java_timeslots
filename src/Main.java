import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    static String date_to_get_timeslots = "";
    static String day_diff_string = null;
    static String format_duration_string;
    static String free_duration_string;
    static String time_day_begins;
    static String time_day_ends;


    static DateTimeFormatter formatter;
    static LocalDateTime localDateTime_day_to;
    static LocalDateTime localDateTime_day_from;
    static Duration day_diff = null;
    static long diff_mili = 0;

    static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    static DateFormat format2 = new SimpleDateFormat("hh:mm a");
    static List<Date> all_day_slots = new ArrayList<>(25);
    static List<String> free_timeSLOTS = new ArrayList<>();
    static String[] allocated_task_start_time;
    static String[] allocated_task_stop_time;
    static Duration freetime_duration;
    static Duration ddmili_duration;

    public static void main(String[] args) throws ParseException {
        allocated_task_start_time = new String[]{"07:00", "11:00", "14:00", "16:30"};
        allocated_task_stop_time = new String[]{"09:00", "13:00", "16:00", "18:00"};
        Input();
    }

    private static void Input() throws ParseException {
        System.out.println("Enter Date (e.g. 2022-09-01)");
        date_to_get_timeslots = new Scanner(System.in).nextLine();

        System.out.println("What time does the day begins?  (e.g. 08:00)");

        time_day_begins = new Scanner(System.in).nextLine();

        System.out.println("What time does the day end?  (e.g. 17:00)");
        time_day_ends = new Scanner(System.in).nextLine();

        Intervals(date_to_get_timeslots, time_day_begins, time_day_ends);
    }

    private static void Intervals(String date_to_get_timeslots, String time_day_begins, String time_day_ends) throws ParseException {
        String date_time_day_begins = date_to_get_timeslots + " " + time_day_begins;// Time day starts
        String date_time_day_ends = date_to_get_timeslots + " " + time_day_ends;
        // Time day ends


        Date day_start_time = format.parse(date_time_day_begins);// convert day start to date
        Date day_end_time = format.parse(date_time_day_ends); // convert dya end to date

        all_day_slots.add(day_start_time); // add day begin time at the beginning of the day

        Compute(day_start_time, day_end_time, date_time_day_begins, date_time_day_ends);
    }

    private static void Compute(Date day_start_time, Date day_end_time, String date_time_day_begins, String date_time_day_ends) throws ParseException {
        Date first_allocated_task_date = format.parse(date_to_get_timeslots + " " + allocated_task_start_time[0]);

        String first_allocated_task_date_string = format2.format(first_allocated_task_date); // convert date day start to string
        String day_end_time_string = format.format(day_end_time); // convert date day end to string

        Calendar cal = Calendar.getInstance(), cal2 = Calendar.getInstance();
        cal.setTime(day_start_time);
        Date first_task_time = format.parse(date_to_get_timeslots + " " + allocated_task_start_time[0]);
        cal2.setTime(first_task_time);

        // if day start ie 7:00 am is less than first task time 8:00, set as available timeslots and
        // add to array freetimeslots
        // slot of one hour from 7-8
        if (cal.before(cal2)) {
            String first_task_time_string = format2.format(day_start_time);
            free_timeSLOTS.add(first_task_time_string + " - " + first_allocated_task_date_string);
        }

        // Get each minute between day begins and day ends
        while (cal.getTime().before(day_end_time)) {
            // and add it to the list intervals
            cal.add(Calendar.MINUTE, 1);
            all_day_slots.add(cal.getTime());
        }

        // loop through allocated time
        // get all one hour time slots between day_begins and  i.e. 7-9 (2 timeslots available)
        // Add all avaiable timeslots after day start time

        for (int i = 0; i < allocated_task_start_time.length; i++) {

            Date date2 = format.parse(date_to_get_timeslots + " " + allocated_task_stop_time[i]);

            String totime_string = format2.format(date2);

            for (Date all_day_slot : all_day_slots) {
                if (format2.format(all_day_slot).equals(totime_string)) { //if time is already allocated
                    Date free_slot_stop_time;
                    if (i + 1 == allocated_task_start_time.length) {
                        free_slot_stop_time = format.parse(day_end_time_string); // if end of all allocated time array, set the slot end time to day end time

                    } else {

                        free_slot_stop_time = format.parse(date_to_get_timeslots + " " + allocated_task_start_time[i + 1]); // else set slot time to next task start time
                    }

                    String free_slot_stop_time_string = format2.format(free_slot_stop_time);

                    free_timeSLOTS.add(totime_string + " - " + free_slot_stop_time_string);
                }
            }

            // calculate duration between day start and day end time
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            localDateTime_day_from = LocalDateTime.parse(date_time_day_begins, formatter); // whole day format from string
            localDateTime_day_to = LocalDateTime.parse(date_time_day_ends, formatter); // whole day format from string

            day_diff = Duration.between(localDateTime_day_from, localDateTime_day_to); // duration for the whole day

            day_diff_string = String.format("%d hour(s) %02d minutes", day_diff.toHoursPart(),
                    day_diff.toMinutesPart()); // whole day duration format to string

            try {

                LocalDateTime lDateTime1 = LocalDateTime.parse(date_to_get_timeslots + " " + allocated_task_start_time[i], formatter); // from time
                // localdatetime
                LocalDateTime lDateTime2 = LocalDateTime.parse(date_to_get_timeslots + " " + allocated_task_stop_time[i], formatter); // to time local date time

                Duration diff = Duration.between(lDateTime1, lDateTime2); // duration between task from time and task to
                // time
                long temp = diff.toMillis(); // temporary value to hold diff converted to milliseconds
                diff_mili = temp + diff_mili; // sum of all tasks duration for the day

                format_duration_string = String.format("%d hour(s) %02d minutes", diff.toHoursPart(),
                        diff.toMinutesPart());

            } catch (Exception e) {
                System.out.println("An Error occured!-->" + e);
            }

        }

        // Output Tie slots available
        System.out.println("\n\nFree time Slots Available Between " + time_day_begins + " to " + time_day_ends);
        for (String free_timeSLOT : free_timeSLOTS) {
            System.out.println(free_timeSLOT);
        }
        // Output the time taken by the allocated tasks
        ddmili_duration = Duration.ofMillis(diff_mili); // milliseconds to duration
        format_duration_string = String.format("%d hour(s) %02d minutes", ddmili_duration.toHoursPart(),
                ddmili_duration.toMinutesPart());
        System.out.println("\n\nSum of time taken allocate tasks is " + format_duration_string);

        //Display day duration and breaks
        long day_diff_mili = day_diff.toMillis() - ddmili_duration.toMillis();
        freetime_duration = Duration.ofMillis(day_diff_mili);
        free_duration_string = String.format("%d hour(s) %02d minutes", freetime_duration.toHoursPart(),
                freetime_duration.toMinutesPart());
        System.out.println("All day duration is " + day_diff_string + " and available Breaks duration " + free_duration_string); // output
    }
}
