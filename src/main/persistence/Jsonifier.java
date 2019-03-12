package persistence;


import model.DueDate;
import model.Priority;
import model.Tag;
import model.Task;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

// Converts model elements to JSON objects
public class Jsonifier {

    // EFFECTS: returns JSON representation of tag
    public static JSONObject tagToJson(Tag tag) {
        JSONObject tagJson = new JSONObject();
        tagJson.put("name", tag.getName());
        return tagJson;
    }

    // EFFECTS: returns JSON representation of priority
    public static JSONObject priorityToJson(Priority priority) {
        JSONObject priorityJson = new JSONObject();
        priorityJson.put("important", priority.isImportant());
        priorityJson.put("urgent", priority.isUrgent());
        return priorityJson;
    }

    // EFFECTS: returns JSON representation of dueDate
    public static JSONObject dueDateToJson(DueDate dueDate) {
        JSONObject dueDateJson = new JSONObject();
        if (dueDate == null) {
            dueDateJson = null;
        } else {
            Calendar dueDateCalendar = Calendar.getInstance();
            dueDateCalendar.setTime(dueDate.getDate());
            dueDateJson.put("year", dueDateCalendar.get(Calendar.YEAR));
            dueDateJson.put("month", dueDateCalendar.get(Calendar.MONTH));
            dueDateJson.put("day", dueDateCalendar.get(Calendar.DAY_OF_MONTH));
            dueDateJson.put("hour", dueDateCalendar.get(Calendar.HOUR_OF_DAY));
            dueDateJson.put("minute", dueDateCalendar.get(Calendar.MINUTE));
        }
        return dueDateJson;
    }

    // EFFECTS: returns JSON representation of task
    public static JSONObject taskToJson(Task task) {
        JSONObject taskJson = new JSONObject();
        taskJson.put("description", task.getDescription());
        taskJson.put("tags", tagsToJsonArray(task.getTags()));
        taskJson.put("due-date", JSONObject.wrap(dueDateToJson(task.getDueDate())));
        taskJson.put("priority", priorityToJson(task.getPriority()));
        taskJson.put("status", task.getStatus());
        return taskJson;
    }

    private static JSONArray tagsToJsonArray(Set<Tag> tags) {
        JSONArray tagsJsonArray = new JSONArray();
        for (Tag t : tags) {
            tagsJsonArray.put(tagToJson(t));
        }
        return tagsJsonArray;
    }

    // EFFECTS: returns JSON array representing list of tasks
    public static JSONArray taskListToJson(List<Task> tasks) {
        JSONArray tasksJsonArray = new JSONArray();
        for (Task t : tasks) {
            tasksJsonArray.put(taskToJson(t));
        }
        return tasksJsonArray;
    }
}
