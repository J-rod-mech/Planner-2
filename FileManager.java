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
        File myObj = new File(directory + "\\daily\\taskdata_" + Planner.zonedDate + ".txt");
        try {
            Scanner fileSC = new Scanner(myObj).useDelimiter("\n");
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
    
    public static void writeFile() {
        try {
            FileWriter myWriter = new FileWriter(directory + "\\daily\\taskdata_" + Planner.zonedDate + ".txt");
            String out = "";
            for (Task t : Planner.tasks) {
                out += "\n";
                out += t.getName() + DELIM + t.getTag() + DELIM + t.isComplete() + DELIM
                        + t.getStart() + DELIM + t.getEnd() + DELIM + t.getNote();
            }
            
            myWriter.write(out);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
