package planner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import static planner.Planner.directory;
import static planner.Planner.DELIM;
import static planner.Planner.timeZone;
import static planner.Planner.myFormat;
import static planner.Planner.HALF_HOURS;
import static planner.Planner.HOUR_OFFSET;
import static planner.Planner.FIFTEEN_DIV;
import static planner.Planner.HOUR_DIV;
import static planner.Planner.QUAD;
import static planner.Planner.MAX_HOUR;

public class HabitManager {
    final static String[] weekDays = {"Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday", "Sunday"};
    final static String HABIT_SUCCESS = "Habit %s added.";
    final static String DEL_SUCCESS = "Habit %s removed.";
    final static String DEL_ERROR = "Habit %s does not exist.";

    public static void printHabitData() {
        File myObj = new File(directory + "\\habitdata.txt");
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
                System.out.print((HALF_HOURS - (HOUR_OFFSET + MAX_HOUR - start) / QUAD
                        % HALF_HOURS) + ":" + (start * FIFTEEN_DIV % HOUR_DIV));
                if (start % QUAD == 0)
                    System.out.print("0");
                if (start / HALF_HOURS / QUAD % 2 == 0)
                    System.out.print(" A");
                else
                    System.out.print(" P");
                System.out.print("M to ");
                System.out.print((HALF_HOURS - (HOUR_OFFSET + MAX_HOUR - end) / QUAD
                        % HALF_HOURS) + ":" + (end * FIFTEEN_DIV % HOUR_DIV));
                if (end % QUAD == 0)
                    System.out.print("0");
                if (end / HALF_HOURS / QUAD % 2 == 0)
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

                        System.out.print(weekDays[i]);
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
        File myObj = new File(directory + "\\habitdata.txt");
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
            FileWriter myWriter = new FileWriter(directory + "\\habitdata.txt");
            myWriter.write(out);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(String.format(HABIT_SUCCESS, task));
    }

    public static void removeHabit(String task) {
        String out = "";
        File myObj = new File(directory + "\\habitdata.txt");
        Scanner fileSC = null;
        boolean found = false;

        try {
            fileSC = new Scanner(myObj).useDelimiter(DELIM);
            out += fileSC.next();
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
                    out += DELIM + first;
                    for (int i = 0; i < 5; i++) {
                        out += DELIM + fileSC.next();
                    }
                    if (fileSC.hasNext())
                        out += DELIM + fileSC.next();
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
            FileWriter myWriter = new FileWriter(directory + "\\habitdata.txt");
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
}
