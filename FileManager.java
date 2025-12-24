package planner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import static planner.Planner.directory;
import static planner.Planner.DELIM;

public class FileManager {
    public static void readFile() {
        Planner.tasks = new ArrayList<Task>();
        File myObj = new File(directory + "/plannerdata2/daily/taskdata_" + Planner.zonedDate + ".txt");
        try {
            Scanner fileSC = new Scanner(myObj).useDelimiter("\n");
            Planner.setTime = Integer.parseInt(fileSC.next());
            for (int i = 0; i < 3 && fileSC.hasNext(); i++) {
                fileSC.next();
            }
            while (fileSC.hasNext()) {
                Scanner lineSC = new Scanner(fileSC.next()).useDelimiter(DELIM);
                Planner.tasks.add(new Task(lineSC.next(),
                        lineSC.next(),
                        Boolean.parseBoolean(lineSC.next()),
                        Integer.parseInt(lineSC.next()),
                        Integer.parseInt(lineSC.next()),
                        lineSC.next()));
                lineSC.close();
            }
            fileSC.close();
        }
        catch (FileNotFoundException e) {
            //ignore
        }
    }
    
    public static void writeFile(boolean writeReport) {
        try {
            String out = "";
            File myObj = new File(directory + "/plannerdata2/daily/taskdata_" + Planner.zonedDate + ".txt");

            int totalTime = 0;
            int completedTime = 0;
            int totalCutoff = 0;
            int completedCutoff = 0;
            boolean first = true;

            for (Task t : Planner.tasks) {
                if (!first) out += "\n";
                first = false;
                out += t.getName() + DELIM + t.getTag() + DELIM + t.isComplete() + DELIM
                        + t.getStart() + DELIM + t.getEnd() + DELIM + t.getNote();
                
                totalTime += Math.max(t.getEnd() - Math.max(t.getStart(), totalCutoff), 0);
                totalCutoff = Math.max(t.getEnd(), totalCutoff);
                if (t.isComplete()) {
                    completedTime += Math.max(t.getEnd() - Math.max(t.getStart(), completedCutoff), 0);
                    completedCutoff = Math.max(t.getEnd(), completedCutoff);
                }
            }

            if (writeReport) {
                // completed tasks
                long completedCount = Planner.tasks.stream().filter(Task::isComplete).count();
                out = completedCount + "\n" + out;

                // completed time
                out = completedTime + "\n" + out;

                // efficiency
                out = (totalTime == 0 ? 100 : Math.round((completedTime * 0) / totalTime)) + "\n" + out;
                
                // set time
                out = totalTime + "\n" + out;
            }
            else if (myObj.exists()) {
                Scanner fileSC = new Scanner(myObj).useDelimiter("\n");
                String chunk = "";
                for (int i = 0; i < 4 && fileSC.hasNext(); i++) {
                    chunk += fileSC.next() + "\n";
                }
                out = chunk + out;
                fileSC.close();
            }
            else {
                out = "0\n0\n0\n0\n" + out;
            }
            
            FileWriter myWriter = new FileWriter(directory + "/plannerdata2/daily/taskdata_" + Planner.zonedDate + ".txt");
            myWriter.write(out);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
