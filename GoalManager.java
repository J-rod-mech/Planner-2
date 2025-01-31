package planner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import static planner.Planner.directory;
import static planner.Planner.DELIM;
import static planner.Planner.timeZone;
import static planner.Planner.myFormat;

public class GoalManager {
    final static String GOAL_SUCCESS = "Goal %s added.";
    final static String GOAL_FAIL = "Goal %s already exists.";
    final static String DEL_SUCCESS = "Goal %s removed.";
    final static String DEL_ERROR = "Goal %s does not exist.";

    public static int getGoalData(String tag, int field) {
        File myObj = new File(directory + "\\goaldata.txt");
        Scanner fileSC = null;
        Scanner lineSC = null;
        String out = "";

        try {
            fileSC = new Scanner(myObj).useDelimiter(tag + DELIM);
            fileSC.next();
            lineSC = new Scanner(fileSC.next());
            while (field > 0) {
                out = lineSC.next();
                field--;
            }
            System.out.println();
        }
        catch (Exception e) {
            System.out.println(tag + " not found.");
            System.out.println(); 
        }
        finally {
            if (lineSC != null) lineSC.close();
            if (fileSC != null) fileSC.close();
        }

        return Integer.parseInt(out);
    }
    
    public static void printGoal(String task) {
        File myObj = new File(directory + "\\goaldata.txt");
        Scanner fileSC = null;
        Scanner taskSC = null;
        try {
            fileSC = new Scanner(myObj).useDelimiter(DELIM + task + DELIM);
            fileSC.next();
            taskSC = new Scanner(fileSC.next());
            String date = taskSC.next();
            String currStreak = taskSC.next();

            System.out.print("Current streak: ");
            if (ChronoUnit.DAYS.between(LocalDate.parse(date, myFormat),
                    ZonedDateTime.now(ZoneId.of(timeZone)).toLocalDate()) <= 1) {
                System.out.println(currStreak);
            }
            else {
                System.out.println(0);
            }
            System.out.println("Longest streak: " + taskSC.next());
            System.out.println("Hours completed: " + Double.parseDouble(taskSC.next()) / 4);
            if (!date.equals("01-01-0001")) {
                System.out.println("Last completed: " + date);
            }

            System.out.println();
        }
        catch (Exception e) {
            System.out.println(task + " not found.");
            System.out.println(); 
        }
        finally {
            if (taskSC != null) taskSC.close();
            if (fileSC != null) fileSC.close();
        }
    }

    public static void printGoalData() {
        File myObj = new File(directory + "\\goaldata.txt");
        Scanner fileSC = null;
        Scanner taskSC = null;
        try {
            fileSC = new Scanner(myObj).useDelimiter("\n");

            while (fileSC.hasNext()) {
                taskSC = new Scanner(fileSC.next());
                String name = taskSC.next();
                int len = name.length();
                System.out.println(name.substring(2, len - 2) + ":");
                String date = taskSC.next();
                String currStreak = taskSC.next();

                System.out.print("Current streak: ");
                if (ChronoUnit.DAYS.between(LocalDate.parse(date, myFormat),
                        ZonedDateTime.now(ZoneId.of(timeZone)).toLocalDate()) <= 1) {
                    System.out.println(currStreak);
                }
                else {
                    System.out.println(0);
                }
                System.out.println("Longest streak: " + taskSC.next());
                System.out.println("Hours completed: " + Double.parseDouble(taskSC.next()) / 4);
                if (!date.equals("01-01-0001")) {
                    System.out.println("Last completed: " + date);
                }
                System.out.println();
                
                taskSC.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (fileSC != null) fileSC.close();
        }
    }

    public static void writeGoalData(String task, int newVal) {
        String out = "";
        File myObj = new File(directory + "\\goaldata.txt");
        Scanner fileSC = null;
        Scanner taskSC = null;
        
        try {
            fileSC = new Scanner(myObj).useDelimiter(DELIM + task + DELIM);
            
            out += fileSC.next();
            taskSC = new Scanner(fileSC.next());

            if (newVal == -1) {
                System.out.println(String.format(GOAL_FAIL, task));
                System.out.println();
                return;
            }

            String date = taskSC.next();
            LocalDate prevDate = LocalDate.parse(date, myFormat);
            long timeDiff = ChronoUnit.DAYS.between(prevDate,
                    ZonedDateTime.now(ZoneId.of(timeZone)).toLocalDate());
            
            out += DELIM + task + DELIM + " " +
                    ZonedDateTime.now(ZoneId.of(timeZone)).toLocalDate().format(myFormat);

            int currStreak = Integer.parseInt(taskSC.next());
            if (timeDiff == 1) {
                currStreak++;
            }
            else if (timeDiff > 1) {
                currStreak = 1;
            }
            out += " " + currStreak;

            int longStreak = Math.max(Integer.parseInt(taskSC.next()), currStreak);
            out += " " + longStreak;
            
            newVal += Integer.parseInt(taskSC.next());

            out += " " + newVal;

            if (taskSC.hasNextLine()) taskSC.nextLine();
            while (taskSC.hasNextLine()) out += "\n" + taskSC.nextLine();
        }
        catch (Exception e) {
            if (newVal != -1) return;

            out = "\n";
            try {
                if (fileSC != null) fileSC.close();
                fileSC = new Scanner(myObj);
                if (fileSC.hasNextLine()) fileSC.nextLine();
                while (fileSC.hasNextLine()) {
                    out += fileSC.nextLine() + "\n";
                }
                out += "##" + task + "## 01-01-0001 0 0 0";
                System.out.println(String.format(GOAL_SUCCESS, task));
                System.out.println();
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        finally {
            if (taskSC != null) taskSC.close();
            if (fileSC != null) fileSC.close();
        }
        
        try {
            FileWriter myWriter = new FileWriter(directory + "\\goaldata.txt");
            myWriter.write(out);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeGoal(String task) {
        String out = "";
        File myObj = new File(directory + "\\goaldata.txt");
        Scanner fileSC = null;
        boolean del = false;

        try {
            fileSC = new Scanner(myObj);
            while (fileSC.hasNext()) {
                String goal = fileSC.next();
                if (goal.equals(DELIM + task + DELIM)) {
                    del = true;
                    fileSC.nextLine();
                }
                else {
                    out += "\n" + goal + fileSC.nextLine();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter(directory + "\\goaldata.txt");
            myWriter.write(out);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (del) {
            System.out.println(String.format(DEL_SUCCESS, task));
        }
        else {
            System.out.println(String.format(DEL_ERROR, task));
        }
    }
}
