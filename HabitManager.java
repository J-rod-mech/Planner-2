package planner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Scanner;
<<<<<<< HEAD
import static planner.Planner.DELIM;
import static planner.Planner.HALF_DIV;
import static planner.Planner.HALF_HOURS;
import static planner.Planner.HOUR_DIV;
import static planner.Planner.MAX_HOUR;
import static planner.Planner.directory;
import static planner.Planner.myFormat;
import static planner.Planner.timeZone;
=======
import static planner.Planner.directory;
import static planner.Planner.myFormat;
import static planner.Planner.timeZone;
import static planner.Planner.DELIM;
import static planner.Planner.HALF_HOURS;
import static planner.Planner.HOUR_OFFSET;
import static planner.Planner.FIFTEEN_DIV;
import static planner.Planner.HOUR_DIV;
import static planner.Planner.QUAD;
import static planner.Planner.MAX_HOUR;
import static planner.Planner.HALF_DIV;
>>>>>>> f783ad6b70443919f7eac5db2b78a6e748d2b2fa

public class HabitManager {
    final static String[] weekDays = {"Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday", "Sunday"};
    final static String HABIT_SUCCESS = "Habit %s added.";
    final static String DEL_SUCCESS = "Habit %s removed.";
    final static String DEL_ERROR = "Habit %s does not exist.";

    public static void printHabitData() {
<<<<<<< HEAD
        File myObj = new File(directory + "/habitdata.txt");
=======
        File myObj = new File(directory + "/plannerdata2/habitdata.txt");
>>>>>>> f783ad6b70443919f7eac5db2b78a6e748d2b2fa
        Scanner fileSC = null;
        try {
            fileSC = new Scanner(myObj).useDelimiter(DELIM);
            if (fileSC.hasNextLine()) fileSC.nextLine();

            while (fileSC.hasNext()) {
                String name = fileSC.next();
                String tag = fileSC.next();
                String sched = fileSC.next();
                int start = Integer.parseInt(fileSC.next());
                int end = Integer.parseInt(fileSC.next());
                String note = fileSC.next();

                System.out.print(name);
                if (!tag.equals(" "))
                     System.out.print(" # " + tag);
                System.out.println(":");

                System.out.print("From ");
                System.out.print((HALF_HOURS - (HOUR_DIV + MAX_HOUR - start) / HOUR_DIV
                        % HALF_HOURS) + ":" + String.format("%02d", start % HOUR_DIV));
                if (start < MAX_HOUR / HALF_DIV)
                    System.out.print(" A");
                else
                    System.out.print(" P");
                System.out.print("M to ");
                System.out.print((HALF_HOURS - (HOUR_DIV + MAX_HOUR - end) / HOUR_DIV
                        % HALF_HOURS) + ":" + String.format("%02d", end % HOUR_DIV));
                if (end < MAX_HOUR / HALF_DIV)
                    System.out.print(" A");
                else
                    System.out.print(" P");
                System.out.println("M.");

                int count = 0;
                for (int i = 0; i < weekDays.length; i++) {
                    if (sched.charAt(i) == '1')
                        count++;
                }

                System.out.print("Set for ");
                for (int i = 0; i < weekDays.length; i++) {
                    if (sched.charAt(i) == '1') {
                        if (count > 1 && (i == weekDays.length - 1 || Integer.parseInt(sched.substring(i + 1)) == 0))
                            System.out.print("and ");

                        System.out.print(weekDays[i].substring(0, 3).toLowerCase());
                        if (i < weekDays.length - 1) {
                            if (count > 2 && Integer.parseInt(sched.substring(i + 1)) != 0) {
                                System.out.print(",");
                            }
                            if (count > 1 && Integer.parseInt(sched.substring(i + 1)) != 0)
                                System.out.print(" ");
                        }   
                    }
                }
                System.out.println(".");

                if (!note.equals(" "))
                    System.out.println("\"" + note + "\"");

                if (fileSC.hasNext())
                    System.out.println();
                
                if (fileSC.hasNext()) fileSC.next();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (fileSC != null) fileSC.close();
        }

        System.out.println();
    }

    public static void addHabit(String task, String tag, String sched,
            int start, int end, String note) {
        String out = "";
<<<<<<< HEAD
        File myObj = new File(directory + "/habitdata.txt");
=======
        File myObj = new File(directory + "/plannerdata2/habitdata.txt");
>>>>>>> f783ad6b70443919f7eac5db2b78a6e748d2b2fa
        Scanner fileSC = null;
        
        try {
            fileSC = new Scanner(myObj);
            while (fileSC.hasNextLine()) {
                out += fileSC.nextLine() + "\n";
            }

            out += DELIM + task + DELIM + tag + DELIM + sched + DELIM + start +
                    DELIM + end + DELIM + note + DELIM;
        }
        catch (Exception e) {
            //ignore
        }
        finally {
            if (fileSC != null) fileSC.close();
        }
        
        try {
<<<<<<< HEAD
            FileWriter myWriter = new FileWriter(directory + "/habitdata.txt");
=======
            FileWriter myWriter = new FileWriter(directory + "/plannerdata2/habitdata.txt");
>>>>>>> f783ad6b70443919f7eac5db2b78a6e748d2b2fa
            myWriter.write(out);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(String.format(HABIT_SUCCESS, task));
    }

    public static void removeHabit(String task) {
        String out = "";
<<<<<<< HEAD
        File myObj = new File(directory + "/habitdata.txt");
=======
        File myObj = new File(directory + "/plannerdata2/habitdata.txt");
>>>>>>> f783ad6b70443919f7eac5db2b78a6e748d2b2fa
        Scanner fileSC = null;
        boolean found = false;

        try {
            fileSC = new Scanner(myObj).useDelimiter(DELIM);
            out += fileSC.nextLine();
            while (fileSC.hasNext()) {
                String first = fileSC.next();
                if (first.equals(task)) {
                    found = true;
                    for (int i = 0; i < 5; i++) {
                        fileSC.next();
                    }
                    if (fileSC.hasNext())
                        fileSC.next();
                }
                else {
                    out += "\n" + DELIM + first;
                    for (int i = 0; i < 5; i++) {
                        out += DELIM + fileSC.next();
                    }
                    if (fileSC.hasNext()) {
                        fileSC.next();
                    }
                    out += DELIM;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (fileSC != null) fileSC.close();
        }
        
        try {
<<<<<<< HEAD
            FileWriter myWriter = new FileWriter(directory + "/habitdata.txt");
=======
            FileWriter myWriter = new FileWriter(directory + "/plannerdata2/habitdata.txt");
>>>>>>> f783ad6b70443919f7eac5db2b78a6e748d2b2fa
            myWriter.write(out);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (found)
            System.out.println(String.format(DEL_SUCCESS, task));
        else
            System.out.println(String.format(DEL_ERROR, task));
    }

    public static void transferHabits() {
        String out = "";
<<<<<<< HEAD
        File myObj = new File(directory + "/habitdata.txt");
=======
        File myObj = new File(directory + "/plannerdata2/habitdata.txt");
>>>>>>> f783ad6b70443919f7eac5db2b78a6e748d2b2fa
        Scanner fileSC = null;
        String date = "NOT FOUND";
        LocalDate now = ZonedDateTime.now(ZoneId.of(timeZone)).toLocalDate();

        try {
            fileSC = new Scanner(myObj).useDelimiter(DELIM);

            if (fileSC.hasNext()) {
                date = fileSC.next().substring(0, 10);
            }
            
            if (!LocalDate.parse(date, myFormat).isBefore(now)) {
                return;
            }

            out += now.format(myFormat);

            while (fileSC.hasNext()) {
                String habit = fileSC.next();
                String tag = fileSC.next();
                String sched = fileSC.next();
                int start = Integer.parseInt(fileSC.next());
                int end = Integer.parseInt(fileSC.next());
                String note = fileSC.next();
                if (fileSC.hasNext()) {
                    fileSC.next();
                }
                
                for (int i = 0; i < weekDays.length; i++) {
                    if (sched.charAt(i) == '1' && now.getDayOfWeek().toString().
                            equals(weekDays[i].toUpperCase())) {
                        Planner.insertTask(new Task(habit, tag, false, start, end, note));
                    }
                }

                out += "\n" + DELIM + habit + DELIM + tag + DELIM + sched +
                        DELIM + start + DELIM + end + DELIM + note + DELIM;
            }
        }
        catch (Exception e) {
            out += now.format(myFormat);
        }
        finally {
            if (fileSC != null) fileSC.close();
        }
        
        try {
<<<<<<< HEAD
            FileWriter myWriter = new FileWriter(directory + "/habitdata.txt");
=======
            FileWriter myWriter = new FileWriter(directory + "/plannerdata2/habitdata.txt");
>>>>>>> f783ad6b70443919f7eac5db2b78a6e748d2b2fa
            myWriter.write(out);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
