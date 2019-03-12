package utility;

import model.Task;
import parsers.TaskParser;
import persistence.Jsonifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// File input/output operations
public class JsonFileIO {
    public static final File jsonDataFile = new File("./resources/json/tasks.json");
    
    // EFFECTS: attempts to read jsonDataFile and parse it
    //           returns a list of tasks from the content of jsonDataFile
    public static List<Task> read() {
        List<Task> tasks = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(jsonDataFile);
            String jsonAsString = "";
            TaskParser taskParser = new TaskParser();

            while (scanner.hasNextLine()) {
                jsonAsString += scanner.nextLine();
            }
            return taskParser.parse(jsonAsString);
        } catch (FileNotFoundException e) {
            System.out.println("tasks.json file not found");
        }
        return tasks;
    }
    
    // EFFECTS: saves the tasks to jsonDataFile
    public static void write(List<Task> tasks) {
        try {
            PrintWriter pw = new PrintWriter(jsonDataFile);

            String pwInput = Jsonifier.taskListToJson(tasks).toString();

            pw.write(pwInput);
            pw.flush();
            pw.close();
        } catch (FileNotFoundException e) {
            System.out.println("tasks.json file not found");
        } catch (IOException e) {
            System.out.println("PrintWriter Error");
        }
    }
}
