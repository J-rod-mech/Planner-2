package planner;

import java.util.Scanner;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.io.File;
import java.util.Arrays;

public class Planner {
    //constants
    final static int HALF_HOURS = 12;
    final static int HALF_DIV = 2;
    final static int HUNDRED_DIV = 100;
    final static int FIFTEEN_DIV = 15;
    final static int MIN_HOUR = 0;
    final static int MAX_HOUR = 96;
    final static int MIN_TIME_REP = 100;
    final static int MAX_TIME_REP = 1245;
    final static int MAX_HOUR_TIME = 2400;
    final static int QUAD = 4;
    final static int HOUR_DIV = 60;
    final static int TENS_DIGIT = 10;
    final static int HOUR_OFFSET = 3;
    final static String ADD_SUCCESS = "%s added.";
    final static String REM_SUCCESS = "%s removed.";
    final static String MOVE_SUCCESS = "%s moved.";
    final static String NOTE_SUCCESS = "Note set to \"%s\".";
    final static String DATE_SUCCESS = "Date changed to %s.";
    final static String TICK_SUCCESS = "%s marked complete.";
    final static String HELP_MENU = "Choose from one of the following commands:\n"
            + "view [v]\n"
            + "add  [a]\n"
            + "rem  [r]\n"
            + "move [m]\n"
            + "note [n]\n"
            + "date [d]\n"
            + "list [l]\n"
            + "help [h]\n"
            + "quit [q]\n";
    final static String HELP_ADD = "add <task>: adds task at the end\n"
            + "add <task> <start> <end>: adds task to time slot\n"
            + "add <#> <tag>: creates tag\n"
            + "add <task> <#> <tag>: adds task with tag\n"
            + "add <task> <#> <tag> <start> <end>: adds task with tag to time slot\n";
    final static String HELP_REM = "rem <task>: removes all instances of a task\n"
            + "rem <task> <instance>: removes instance n of a task\n"
            + "rem <start> <end>: removes all tasks within time slot\n"
            + "rem <#> <tag>: removes tag\n"
            + "rem <task> <#> <tag>: removes tag from task\n";
    final static String HELP_VIEW = "view: displays schedule\n"
            + "view <task> <instance>: displays note for instance n of a task\n";
    final static String HELP_MOVE = "move <task> <+/-> <time>: shifts all instances of a task\n"
            + "move <task> <instance> <+/-> <time>: shifts instance n of a task\n"
            + "move <task> <instance> <start> <end>: moves instance n of a task to time slot\n"
            + "move <start> <end> <+/-> <time>: shifts time slot\n";
    final static String HELP_NOTE = "note <task> <instance>: sets note for instance n of a task\n";
    final static String HELP_DATE = "date <date>: sets current working date\n";
    final static String HELP_LIST = "list: displays all current and future tasks\n"
            + "list <#>: displays all tags\n"
            + "list <#> <tag>: displays all current and future tasks with tag\n";
    final static String HELP_HELP = "help: displays list of commands\n"
            + "help <command>: displays uses of a command\n";
    final static String HELP_QUIT = "quit: quits program\n";
    final static String HELP_TICK = "tick <task>: marks instance n of a task complete\n";
    final static String COMMAND_ERROR = "Invalid command.";
    final static String TIME_ERROR = "Invalid time slot.";
    final static String INC_ERROR = "Invalid time increment.";
    final static String DATE_ERROR = "Invalid date.";
    final static String TASK_ERROR = "%s does not exist.";
    final static String BOUND_ERROR = "%s cannot be moved outside valid time bounds.";
    final static String TICK_ERROR = "%s already completed.";
    final static String REPEAT_ERROR = "Tag %s already exists";
    final static String TAG_SUCCESS = "Tag %s added.";
    final static String TAG_ERROR = "Tag %s does not exist.";
    final static String KEY_ERROR = "Keyword modification not allowed.";
    final static String DELIM = "##";
    final static String KEY_TAG1 = "goal";
    final static String KEY_TAG2 = "habit";
    final static String KEY_TAG3 = "undated";
    
    // custom parameters
    static String timeZone = "UTC-8";
    static String directory = "G:\\My Drive\\Schedule Data";

    static DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("MM-dd-uuuu").withResolverStyle(ResolverStyle.STRICT);
    static String zonedDate = ZonedDateTime.now(ZoneId.of(timeZone)).format(myFormat);
    static ArrayList<Task> tasks = new ArrayList<Task>();



    public static boolean inputEquals(String in, String command) {
        if (command.toLowerCase().equals(in) ||
                command.substring(0, 1).toLowerCase().equals(in)) {
            return true;
        }
        
        return false;
    }

    public static int convertTime(String time) {
        int len = time.length();
        int slot = -1;
        if (len > 1 && time.substring(len - 2).toLowerCase().equals("am")) {
            time = time.substring(0, len - 2);
            try {
                slot = Integer.parseInt(time);
                if (slot < MIN_TIME_REP || slot > MAX_TIME_REP) {
                    return -1;
                }
                slot %= MAX_HOUR_TIME / HALF_DIV;
            }
            catch (NumberFormatException e) {
                return -1;
            }
        }
        else if (len > 1 && time.substring(len - 2).toLowerCase().equals("pm")) {
            time = time.substring(0, len - 2);
            try {
                slot = Integer.parseInt(time);
                if (slot < MIN_TIME_REP || slot > MAX_TIME_REP) {
                    return -1;
                }
                slot = slot % (MAX_HOUR_TIME / HALF_DIV)
                        + MAX_HOUR_TIME / HALF_DIV;
            }
            catch (NumberFormatException e) {
                return -1;
            }
        }
        else {
            try {
                slot = Integer.parseInt(time);
            }
            catch (NumberFormatException e) {
                return -1;
            }
        }

        if (slot < MIN_HOUR || slot > MAX_HOUR_TIME || 
                slot % HUNDRED_DIV % FIFTEEN_DIV != 0 || 
                slot % HUNDRED_DIV / HOUR_DIV != 0) {
                    return -1;
            }
        
        return slot / HUNDRED_DIV * QUAD + slot % HUNDRED_DIV / FIFTEEN_DIV;
    }

    public static int convertInc(String time) {
        int slot = -1;
        try {
            slot = Integer.parseInt(time);
        }
        catch (NumberFormatException e) {
            return -1;
        }

        if (slot < MIN_HOUR || slot > MAX_HOUR_TIME || 
                slot % HUNDRED_DIV % FIFTEEN_DIV != 0 || 
                slot % HUNDRED_DIV / HOUR_DIV != 0) {
                    return -1;
            }
        
        return slot / HUNDRED_DIV * QUAD + slot % HUNDRED_DIV / FIFTEEN_DIV;
    }

    public static Task getTask(String name, String N) {
        int n = -1;
        try {
            n = Integer.parseInt(N);
        }
        catch (NumberFormatException e) {
            return null;
        }

        for (Task t : tasks) {
            if (t.getName().equals(name)) {
                n--;
                if (n == 0) {
                    return t;
                }
            }
        }

        return null;
    }

    public static void insertTask(String name, String tag, int start, int end) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getName().equals(name)) {
                if (start <= tasks.get(i).getStart()) {
                    tasks.add(i, new Task(name, tag, start, end));
                    return;
                }
                else if (i == tasks.size() - 1 ||
                        !tasks.get(i + 1).getName().equals(name)) {
                    tasks.add(i + 1, new Task(name, tag, start, end));
                    return;
                }
            }
        }
        tasks.add(new Task(name, tag, start, end));
    }

    public static void printSchedule() {
        @SuppressWarnings("unchecked")
        ArrayList<String>[] schedule = new ArrayList[MAX_HOUR + 1];
        for (Task t : tasks) {
            for (int i = t.getStart(); i < t.getEnd(); i++) {
                if (schedule[i] == null) schedule[i] = new ArrayList<String>();
                schedule[i].add(t.getName());
            }
        }
        
        boolean isEmpty = true;

        System.out.println(zonedDate + ":");
        for (int i = MIN_HOUR; i <= MAX_HOUR; i++) {
            int j = MAX_HOUR - i;
            if ((schedule[i] != null) && (i == MIN_HOUR || !(schedule[i].equals
                    (schedule[i - 1]))) || i > MIN_HOUR && schedule[i - 1]
                    != null && !schedule[i - 1].equals(schedule[i])) {
                if ((i + 44) % 48 / 36 == 0)
                    System.out.print(" ");
                System.out.print((HALF_HOURS - (HOUR_OFFSET + j) / QUAD
                        % HALF_HOURS) + ":" + (i * FIFTEEN_DIV % HOUR_DIV));
                if (i % QUAD == 0)
                    System.out.print("0");
                if (i / HALF_HOURS / QUAD % 2 == 0)
                    System.out.print(" A");
                else
                    System.out.print(" P");
                System.out.println("M");

                if (schedule[i] != null) {
                    isEmpty = false;
                    System.out.print("  |  ");
                    for (int k = 0; k < schedule[i].size(); k++) {
                        if (k > 0)
                            System.out.print(", ");
                        System.out.print(schedule[i].get(k));
                    }
                }
                System.out.println();
            }
        }
        if (isEmpty)
            System.out.println();
    }

    public static void printList(String tag) {
        String list = "";
        File dir = new File(directory + "\\daily");
        File[] directoryListing = dir.listFiles();
        Arrays.sort(directoryListing);
        if (directoryListing != null) {
            for (int i = 0; i < directoryListing.length; i++) {
                boolean match = false;
                ArrayList<String> taskList = new ArrayList<String>();
                String date = directoryListing[i].getName().substring(9,19);
                if (!LocalDate.parse(date, myFormat).isBefore(ZonedDateTime.now(ZoneId.of(timeZone)).toLocalDate())) {
                    try {
                        Scanner fileSC = new Scanner(directoryListing[i]).useDelimiter("\n");
                        while (fileSC.hasNext()) {
                            Scanner lineSC = new Scanner(fileSC.next()).useDelimiter(DELIM);
                            String in = lineSC.next();
                            String in2 = lineSC.next();
                            if (!taskList.contains(in) && (tag == null || tag.equals(in2))) {
                                list += "[" + ((lineSC.next().equals("true")) ? "X" : " ") + "] "
                                        + date + ": " + in + "\n";
                                taskList.add(in);
                                match = true;
                            }
                            lineSC.close();
                        }
                        fileSC.close();
                    }
                    catch (Exception e) {
                        System.out.println("Could not find schedule.");
                    } 
                    if (taskList.size() > 0 && match) {
                        list += "\n";
                    }
                }
            }
        } else {
            System.out.println("Could not find schedules.");
        }

        System.out.print(list);
    }

    public static void main(String[] args) {
        FileManager.readFile();
        System.out.println(HELP_MENU);
        
        Scanner sc1 = new Scanner(System.in).useDelimiter(System.lineSeparator());
        while (sc1.hasNext()) {
            String in0 = sc1.next();
            Scanner sc2 = new Scanner(in0);

            //quit: quits program
            if (in0.toLowerCase().equals("quit") || in0.toLowerCase().equals("q")) {
                sc2.close();
                sc1.close();
                break;
            }
            
            while (true) {
                if (!sc2.hasNext()) {
                    break;
                }
                String in1 = sc2.next();
                if (inputEquals(in1, "add")) {
                    if (!sc2.hasNext()) {
                        System.out.println(COMMAND_ERROR);
                        System.out.println();
                        break;
                    }
                    
                    //add <task>: adds task at the end
                    String name = sc2.next();
                    if (!sc2.hasNext()) {
                        insertTask(name, " ", MAX_HOUR - 1, MAX_HOUR);
                        System.out.println(String.format(ADD_SUCCESS, name));
                        System.out.println();
                        break;
                    }

                    String in2 = sc2.next();
                    int start = convertTime(in2);

                    //add <#> <tag>: creates tag
                    if (!sc2.hasNext()) {
                        if (name.equals("#")) {
                            if (!TagManager.findTag(in2)) {
                                TagManager.writeTagData(in2);
                                System.out.println(String.format(TAG_SUCCESS, in2));
                            }
                            else {
                                System.out.println(String.format(REPEAT_ERROR, in2));
                            }
                        }
                        else {
                            System.out.println(COMMAND_ERROR);
                        }
                        System.out.println();
                        break;
                    }

                    String in3 = sc2.next();
                    int end = convertTime(in3);
                    if (!sc2.hasNext()) {
                        //add <task> <#> <tag>: adds task with tag
                        if (in2.equals("#")) {
                            if (in3.toLowerCase().equals(KEY_TAG1)) {
                                GoalManager.writeGoalData(name, -1);
                                break;
                            }
                            else if (in3.toLowerCase().equals(KEY_TAG2)) {
                                //TODO: habit functionality
                            }
                            else if (in3.toLowerCase().equals(KEY_TAG3)) {
                                //TODO: undated functionality
                            }
                            else {
                                if (TagManager.findTag(in3)) {
                                    insertTask(name, in3, MAX_HOUR - 1, MAX_HOUR);
                                    System.out.println(String.format(ADD_SUCCESS, name));
                                }
                                else {
                                    System.out.println(String.format(TAG_ERROR, in3));
                                }
                                System.out.println();
                                break;
                            }
                            break;
                        }

                        int len = in3.length();
                        if (len > 1 && in3.substring(len - 2).toLowerCase().
                                equals("am") && end == 0) {
                            end = MAX_HOUR;
                        }

                        //add <task> <start> <end>: adds task to time slot
                        if (end > start && start >= 0) {
                            insertTask(name, " ", start, end);
                            System.out.println(String.format(ADD_SUCCESS, name));
                            System.out.println();
                            break;
                        }
                        else {
                            System.out.println(TIME_ERROR);
                            System.out.println();
                            break;
                        }
                    }

                    start = convertTime(sc2.next());
                    if (!sc2.hasNext()) {
                        System.out.println(COMMAND_ERROR);
                        System.out.println();
                        break;
                    }

                    end = convertTime(sc2.next());
                    if (sc2.hasNext()) {
                        System.out.println(COMMAND_ERROR);
                        System.out.println();
                        break;
                    }
                    
                    //add <task> <#> <tag> <start> <end>: adds task with tag to time slot
                    if (!in2.equals("#")) {
                        System.out.println(COMMAND_ERROR);
                        System.out.println();
                        break;
                    }

                    if (!TagManager.findTag(in3)) {
                        System.out.println(String.format(TAG_ERROR, in3));
                        System.out.println();
                        break;
                    }

                    if (end > start && start >= 0) {
                        insertTask(name, in3, start, end);
                        System.out.println(String.format(ADD_SUCCESS, name));
                        System.out.println();
                        break;
                    }
                    else {
                        System.out.println(TIME_ERROR);
                        System.out.println();
                        break;
                    }
                }
                else if (inputEquals(in1, "rem")) {
                    if (!sc2.hasNext()) {
                        System.out.println(COMMAND_ERROR);
                        System.out.println();
                        break;
                    }
                    String in2 = sc2.next();

                    //rem <task>: removes all instances of a task
                    if (!sc2.hasNext()) {
                        if (getTask(in2, "1") == null) {
                            System.out.println(String.format(TASK_ERROR, in2));
                            System.out.println();
                            break;
                        }

                        for (int i = 0; i < tasks.size(); i++) {
                            if (tasks.get(i).getName().equals(in2)) {
                                tasks.remove(i);
                                i--;
                            }
                        }
                        System.out.println(String.format(REM_SUCCESS, in2));
                        System.out.println();
                        break;
                    }
                    
                    String in3 = sc2.next();
                    if (!sc2.hasNext()) {
                        int n = -1;
                        try {
                            n = Integer.parseInt(in3);
                        }
                        catch (NumberFormatException e) {
                            //ignore
                        }
                        int start = convertTime(in2);
                        int end = convertTime(in3);
    
                        int len = in3.length();
                        if (len > 1 && in3.substring(len - 2).toLowerCase().
                                equals("am") && end == 0) {
                            end = MAX_HOUR;
                        }
    
                        //rem <#> <tag>: removes tag
                        if (in2.equals("#")) {
                            if (in3.equals(KEY_TAG1) || in3.equals(KEY_TAG2) ||
                                    in3.equals(KEY_TAG3)) {
                                System.out.println(KEY_ERROR);
                            }
                            else {
                                TagManager.removeTag(in3);
                            }
                            System.out.println();
                            break;
                        }
    
                        //rem <task> <instance>: removes instance n of a task
                        if (getTask(in2, in3) != null) {
                            for (int i = 0; i < tasks.size(); i++) {
                                if (tasks.get(i).getName().equals(in2)) {
                                    n--;
                                    if (n == 0) {
                                        tasks.remove(i);
                                        break;
                                    }
                                }
                            }
                            System.out.println(String.format(REM_SUCCESS, in2 + " " + in3));
                        }
    
                        //rem <start> <end>: removes all tasks within time slot
                        else if (start >= 0) {
                            if (end > start) {
                                for (int i = 0; i < tasks.size(); i++) {
                                    if (start < tasks.get(i).getEnd() &&
                                            end > tasks.get(i).getStart()) {
                                        tasks.remove(i);
                                        i--;
                                    }
                                }
                                System.out.println(String.format(REM_SUCCESS, "Time slot"));
                            }
                            else {
                                System.out.println(TIME_ERROR);
                            }
                        }
                        else {
                            System.out.println(String.format(TASK_ERROR, in2 + " " + in3));
                        }

                        System.out.println();
                        break;
                    }
                    
                    //rem <task> <#> <tag>: removes tag from task
                    String in4 = sc2.next();
                    if (sc2.hasNext()) {
                        System.out.println(COMMAND_ERROR);
                        System.out.println();
                        break;
                    }

                    if (!in3.equals("#")) {
                        System.out.println(COMMAND_ERROR);
                        System.out.println();
                        break;
                    }

                    if (in4.equals(KEY_TAG1)) {
                        GoalManager.removeGoal(in2);
                    }
                    else if (in4.equals(KEY_TAG2)) {
                        //TODO
                    }
                    else if (in4.equals(KEY_TAG3)) {
                        //TODO
                    }
                    else if (TagManager.findTag(in4)) {
                        //TODO
                    }
                    else {
                        System.out.println(String.format(TAG_ERROR, in4));
                    }

                    System.out.println();
                    break;
                }

                //view: displays schedule
                else if (inputEquals(in1, "view")) {
                    if (!sc2.hasNext()) {
                        printSchedule();
                        break;
                    }

                    String in2 = sc2.next();
                    if (!sc2.hasNext()) {
                        System.out.println(COMMAND_ERROR);
                        System.out.println();
                        break;
                    }

                    String in3 = sc2.next();
                    if (sc2.hasNext()) {
                        System.out.println(COMMAND_ERROR);
                        System.out.println();
                        break;
                    }

                    //view <task> <instance>: displays note for instance n of a task
                    Task task = getTask(in2, in3);
                    if (task == null) {
                        System.out.println(String.format(TASK_ERROR, in2 + " " + in3));
                        System.out.println();
                        break;
                    }

                    System.out.println("\"" + task.getNote() + "\"");
                    System.out.println();
                    break;
                }

                //help: displays list of commands
                else if (inputEquals(in1, "help")) {
                    if (!sc2.hasNext()) {
                        System.out.println(HELP_MENU);
                        break;
                    }
                    String in2 = sc2.next();

                    if (sc2.hasNext()) {
                        System.out.println(COMMAND_ERROR);
                        System.out.println();
                        break;
                    }
                    
                    //help <command>: displays uses of a command
                    if (inputEquals(in2, "view")) {
                        System.out.println(HELP_VIEW);
                    }
                    else if (inputEquals(in2, "add")) {
                        System.out.println(HELP_ADD);
                    }
                    else if (inputEquals(in2, "rem")) {
                        System.out.println(HELP_REM);
                    }
                    else if (inputEquals(in2, "move")) {
                        System.out.println(HELP_MOVE);
                    }
                    else if (inputEquals(in2, "note")) {
                        System.out.println(HELP_NOTE);
                    }
                    else if (inputEquals(in2, "date")) {
                        System.out.println(HELP_DATE);
                    }
                    else if (inputEquals(in2, "list")) {
                        System.out.println(HELP_LIST);
                    }
                    else if (inputEquals(in2, "help")) {
                        System.out.println(HELP_HELP);
                    }
                    else if (inputEquals(in2, "quit")) {
                        System.out.println(HELP_QUIT);
                    }
                    else if (inputEquals(in2, "quit")) {
                        System.out.println(HELP_TICK);
                    }
                    else {
                        System.out.println(COMMAND_ERROR);
                        System.out.println();
                    }

                    break;
                }
                else if (inputEquals(in1, "list")) {
                    //list: displays all current and future tasks
                    if (!sc2.hasNext()) {
                        printList(null);
                        break;
                    }
                    
                    String in2 = sc2.next();
                    if (!sc2.hasNext()) {
                        //list <#>: displays all tags
                        if (in2.equals("#")) {
                            System.out.println(KEY_TAG1);
                            System.out.println(KEY_TAG2);
                            System.out.println(KEY_TAG3);
                            TagManager.printTagData();
                        }
                        else {
                            System.out.println(COMMAND_ERROR);
                            System.out.println();
                        }
                        break;
                    }

                    String in3 = sc2.next();
                    if (sc2.hasNext() || !in2.equals("#")) {
                        System.out.println(COMMAND_ERROR);
                        System.out.println();
                        break;
                    }
                    
                    //list <#> <tag>: displays all current and future tasks with tag
                    if (in3.equals(KEY_TAG1)) {
                        GoalManager.printGoalData();
                    }
                    else if (in3.equals(KEY_TAG2)) {
                        
                    }
                    else if (in3.equals(KEY_TAG3)) {

                    }
                    else if (TagManager.findTag(in3)) {
                        printList(in3);
                    }

                    break;
                }

                //date <date>: sets current working date
                else if (inputEquals(in1, "date")) {
                    if (!sc2.hasNext()) {
                        System.out.println(COMMAND_ERROR);
                        System.out.println();
                        break;
                    }
                    String in2 = sc2.next();
                    
                    if (sc2.hasNext()) {
                        System.out.println(COMMAND_ERROR);
                        System.out.println();
                        break;
                    }
                    try {
                        if (in2.length() == 10) {
                            LocalDate.parse(in2, myFormat);
                            zonedDate = in2;
                        }
                        else if (in2.length() == 5) {
                            LocalDate.parse(in2 + zonedDate.substring(5), myFormat);
                            zonedDate = in2 + zonedDate.substring(5);
                        }
                        else if (in2.length() == 2) {
                            LocalDate.parse(zonedDate.substring(0, 3) + in2 + zonedDate.substring(5), myFormat);
                            zonedDate = zonedDate.substring(0, 3) + in2 + zonedDate.substring(5);
                        }
                        else {
                            System.out.println(DATE_ERROR);
                            System.out.println();
                            break;
                        }
                        FileManager.readFile();
                        System.out.println(String.format(DATE_SUCCESS, zonedDate));
                        System.out.println();
                    }
                    catch(Exception e) {
                        System.out.println(DATE_ERROR);
                        System.out.println();
                        break;
                    }
                    break;
                }
                else if (inputEquals(in1, "move")) {
                    if (!sc2.hasNext()) {
                        System.out.println(COMMAND_ERROR);
                        System.out.println();
                        break;
                    }

                    String in2 = sc2.next();
                    if (!sc2.hasNext()) {
                        System.out.println(COMMAND_ERROR);
                        System.out.println();
                        break;
                    }

                    String in3 = sc2.next();
                    if (!sc2.hasNext()) {
                        System.out.println(COMMAND_ERROR);
                        System.out.println();
                        break;
                    }

                    String in4 = sc2.next();
                    
                    //move <task> <+/-> <time>: shifts all instances of a task
                    if (!sc2.hasNext()) { //3 args
                        if (getTask(in2, "1") == null) {
                            System.out.println(String.format(TASK_ERROR, in2));
                            System.out.println();
                            break;
                        }
                        else if (!in3.equals("+") && !in3.equals("-")) {
                            System.out.println(COMMAND_ERROR);
                            System.out.println();
                            break;
                        }

                        int mult = 1;
                        if (in3.equals("-"))
                            mult = -1;

                        int s = convertInc(in4) * mult;
                        if (s * mult < 0) {
                            System.out.println(INC_ERROR);
                            System.out.println();
                            break;
                        }

                        int n = 1;
                        for (Task t : tasks) {
                            if (t.getName().equals(in2)) {
                                if (t.getStart() + s < MIN_HOUR ||
                                        t.getEnd() + s > MAX_HOUR) {
                                    System.out.println(String.format(BOUND_ERROR, in2 + " " + n));
                                }
                                else {
                                    t.setTime(t.getStart() + s, t.getEnd() + s);
                                    System.out.println(String.format(MOVE_SUCCESS, in2 + " " + n));
                                }
                                n++;
                            }
                        }
                        
                        
                        System.out.println();
                        break;
                    }
                    else { //4 args
                        String in5 = sc2.next();
                        if (sc2.hasNext()) {
                            System.out.println(COMMAND_ERROR);
                            System.out.println();
                            break;
                        }
                        
                        Task task = getTask(in2, in3);
                        int bStart = convertTime(in2);
                        int bEnd = convertTime(in3);
                        int start = convertTime(in4);
                        int end = convertTime(in5);

                        int len = in3.length();
                        if (len > 1 && in3.substring(len - 2).toLowerCase().
                                equals("am") && bEnd == 0) {
                            bEnd = MAX_HOUR;
                        }

                        len = in5.length();
                        if (len > 1 && in5.substring(len - 2).toLowerCase().
                                equals("am") && end == 0) {
                            end = MAX_HOUR;
                        }

                        if (task != null) {
                            //move <task> <instance> <start> <end>: moves instance n of a task to time slot
                            if (start >= 0) {
                                if (end > start) {
                                    task.setTime(start, end);
                                    System.out.println(String.format(MOVE_SUCCESS, in2 + " " + in3));
                                }
                                else {
                                    System.out.println(TIME_ERROR);
                                }
                            }

                            //move <task> <instance> <+/-> <time>: shifts instance n of a task
                            else if (in4.equals("+") ||
                                    in4.equals("-")) {
                                int s = convertInc(in5);
                                if (s < 0) {
                                    System.out.println(INC_ERROR);
                                    System.out.println();
                                    break;
                                }
                                
                                if (in4.equals("-")) s *= -1;

                                if (task.getStart() + s < MIN_HOUR ||
                                        task.getEnd() + s > MAX_HOUR) {
                                    System.out.println(String.format(BOUND_ERROR, in2 + " " + in3));
                                }
                                else {
                                    task.setTime(task.getStart() + s, task.getEnd() + s);
                                    System.out.println(String.format(MOVE_SUCCESS, in2 + " " + in3));
                                }
                            }
                            else {
                                System.out.println(COMMAND_ERROR);
                            }
                           
                            System.out.println();
                            break;
                        }
                        
                        //move <start> <end> <+/-> <time>: shifts time slot
                        else if (bStart >= 0){
                            if (bEnd < bStart) {
                                System.out.println(TIME_ERROR);
                            }
                            else if (!in4.equals("+") && !in4.equals("-")) {
                                System.out.println(COMMAND_ERROR);
                            }
                            else if (convertInc(in5) < 0) {
                                System.out.println(INC_ERROR);
                            }
                            else {
                                int s = convertInc(in5);
                                if (in4.equals("-")) s *= -1;

                                int n = -1;
                                String lTask = null;
                                for (Task t : tasks) {
                                    if (bStart < t.getEnd() &&
                                            bEnd > t.getStart()) {
                                        if (t.getName().equals(lTask)) {
                                            n++;
                                        }
                                        else {
                                            n = 1;
                                        }
                                        lTask = t.getName();
                                        if (t.getStart() + s < MIN_HOUR ||
                                                t.getEnd() + s > MAX_HOUR) {
                                            System.out.println(String.format(BOUND_ERROR, lTask + " " + n));
                                        }
                                        else {
                                            t.setTime(t.getStart() + s, t.getEnd() + s);
                                            System.out.println(String.format(MOVE_SUCCESS, lTask + " " + n));
                                        }
                                    }
                                }
                            }

                            System.out.println();
                            break;
                        }

                        System.out.println(String.format(TASK_ERROR, in2 + " " + in3));
                        System.out.println();
                        break;
                    }
                }

                //note <task> <instance>: sets note for instance n of a task
                else if (inputEquals(in1, "note")) {
                    if (!sc2.hasNext()) {
                        System.out.println(COMMAND_ERROR);
                        System.out.println();
                        break;
                    }
                    
                    String in2 = sc2.next();

                    if (!sc2.hasNext()) {
                        System.out.println(COMMAND_ERROR);
                        System.out.println();
                        break;
                    }
                    
                    String in3 = sc2.next();

                    if (sc2.hasNext()) {
                        System.out.println(COMMAND_ERROR);
                        System.out.println();
                        break;
                    }

                    Task task = getTask(in2, in3);
                    if (task == null) {
                        System.out.println(String.format(TASK_ERROR, in2 + " " + in3));
                        System.out.println();
                        break;
                    }

                    sc2.close();
                    Scanner sc3 = null;
                    try {
                        sc3 = new Scanner(sc1.next()).useDelimiter(System.lineSeparator());
                        if (!sc3.hasNext()) {
                            System.out.println("No changes made.");
                            System.out.println();
                            sc3.close();
                            break;
                        }
                        String note = sc3.next();

                        task.setNote(note);
                        System.out.println(String.format(NOTE_SUCCESS, note));
                        System.out.println();
                    }
                    finally {
                        if (sc3 != null) {
                            sc3.close();
                        }
                    }

                    break;
                }

                //tick <task>: marks instance n of a task complete
                else if (inputEquals(in1, "tick")) {
                    if (!sc2.hasNext()) {
                        System.out.println(COMMAND_ERROR);
                        System.out.println();
                        break;
                    }
                    
                    String in2 = sc2.next();

                    if (sc2.hasNext()) {
                        System.out.println(COMMAND_ERROR);
                        System.out.println();
                        break;
                    }
                    
                    Task task = getTask(in2, "1");
                    if (task == null) {
                        System.out.println(String.format(TASK_ERROR, in2));
                        System.out.println();
                        break;
                    }

                    if (task.isComplete()) {
                        System.out.println(String.format(TICK_ERROR, in2));
                        System.out.println();
                        break;
                    }
                    
                    int sum = 0;
                    for (Task t : tasks) {
                        if (t.getName().equals(in2)) {
                            sum += t.getEnd() - t.getStart();
                            t.tick();
                        }
                    }

                    GoalManager.writeGoalData(in2, sum);
                    System.out.println(String.format(TICK_SUCCESS, in2));
                    System.out.println();
                    break;
                }
                else {
                    System.out.println(COMMAND_ERROR);
                    System.out.println();
                    break;
                }
            }
            FileManager.writeFile();
            sc2.close();
        }
    }
}