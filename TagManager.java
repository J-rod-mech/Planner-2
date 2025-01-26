package planner;

import static planner.Planner.directory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class TagManager {
    final static String DEL_SUCCESS = "Tag %s removed.";
    final static String DEL_ERROR = "Tag %s does not exist.";
    
    public static boolean findTag(String tag) {
        File myObj = new File(directory + "\\tagdata.txt");
        Scanner fileSC = null;

        try {
            fileSC = new Scanner(myObj);
            
            while (fileSC.hasNext()) {
                if (fileSC.next().equals(tag)) {
                    return true;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (fileSC != null) fileSC.close();
        }

        return false;
    }
    
    public static void printTagData() {
        File myObj = new File(directory + "\\tagdata.txt");
        Scanner fileSC = null;

        try {
            fileSC = new Scanner(myObj).useDelimiter("\n");

            while (fileSC.hasNext()) {
                System.out.println(fileSC.next());
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

    public static void writeTagData(String tag) {
        String out = "";
        File myObj = new File(directory + "\\tagdata.txt");
        Scanner fileSC = null;

        try {
            fileSC = new Scanner(myObj).useDelimiter("\n");
            
            while (fileSC.hasNext()) {
                out += "\n" + fileSC.next();
            }

            if (!tag.equals(" ")) {
                out += "\n" + tag;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (fileSC != null) fileSC.close();
        }

        try {
            FileWriter myWriter = new FileWriter(directory + "\\tagdata.txt");
            myWriter.write(out);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeTag(String tag) {
        String out = "";
        File myObj = new File(directory + "\\tagdata.txt");
        Scanner fileSC = null;
        boolean del = false;

        try {
            fileSC = new Scanner(myObj);
            while (fileSC.hasNext()) {
                String next = fileSC.next();
                if (!next.equals(tag)) {
                    out += "\n" + next;
                }
                else {
                    del = true;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter(directory + "\\tagdata.txt");
            myWriter.write(out);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (del) {
            System.out.println(String.format(DEL_SUCCESS, tag));
        }
        else {
            System.out.println(String.format(DEL_ERROR, tag));
        }
    }
}
