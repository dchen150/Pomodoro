package parsers;

import model.DueDate;
import model.Priority;
import model.Status;
import model.Task;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// Represents Task parser
public class TaskParser {

    // EFFECTS: iterates over every JSONObject in the JSONArray represented by the input
    // string and parses it as a task; each parsed task is added to the list of tasks.
    // Any task that cannot be parsed due to malformed JSON data is not added to the
    // list of tasks.
    // Note: input is a string representation of a JSONArray
    public List<Task> parse(String input) {
        List<Task> tasks = new ArrayList<>();
        JSONArray tasksArray = new JSONArray(input);

        for (Object object : tasksArray) {
            JSONObject taskJson = (JSONObject) object;
            if (isValidObject(taskJson)) {
                Priority p = new Priority();

                Task t = new Task(taskJson.get("description").toString());
                p.setImportant(taskJson.getJSONObject("priority").getBoolean("important"));
                p.setUrgent(taskJson.getJSONObject("priority").getBoolean("urgent"));
                t.setPriority(p);
                setStatusInParse(t, taskJson);
                t.setDueDate(setDueDateInParse(taskJson));
                setTagsInParse(t, taskJson);
                tasks.add(t);
            }
        }
        return tasks;
    }

    private boolean isValidObject(JSONObject object) {
        return isValidDescription(object)
                && isValidTagsArray(object)
                && isValidDueDateObject(object)
                && isValidPriority(object)
                && isValidStatus(object);
    }

    private boolean isValidStatus(JSONObject obj) {
        if (obj.has("status")) {
            try {
                if (obj.getString("status").equals("DONE")
                        || obj.getString("status").equals("IN_PROGRESS")
                        || obj.getString("status").equals("UP_NEXT")
                        || obj.getString("status").equals("TODO")) {
                    return true;
                }
                return false;
            } catch (JSONException e) {
                return false;
            }
        }
        return false;
    }

    private boolean isValidPriority(JSONObject obj) {
        if (obj.has("priority")) {
            try {
                obj.getJSONObject("priority");
                return isValidPriorityObject(obj);
            } catch (JSONException e) {
                return false;
            }
        }
        return false;
    }

    private boolean isValidPriorityObject(JSONObject obj) {
        try {
            obj.getJSONObject("priority").getBoolean("important");
            obj.getJSONObject("priority").getBoolean("urgent");
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    private boolean isValidDueDateObject(JSONObject obj) {
        try {
            if (obj.get("due-date").equals(JSONObject.NULL)) {
                return true;
            }
            return isValidDueDate(obj);
        } catch (JSONException e) {
            return false;
        }
    }

    private boolean isValidDueDate(JSONObject obj) {
        try {
            obj.getJSONObject("due-date").getInt("year");
            obj.getJSONObject("due-date").getInt("month");
            obj.getJSONObject("due-date").getInt("day");
            obj.getJSONObject("due-date").getInt("hour");
            obj.getJSONObject("due-date").getInt("minute");
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    private boolean isValidTagsArray(JSONObject obj) {
        try {
            obj.getJSONArray("tags");
            return isValidTags(obj);
        } catch (JSONException e) {
            return false;
        }
    }

    private boolean isValidTags(JSONObject obj) {
        for (Object json : obj.getJSONArray("tags")) {
            JSONObject castJson = (JSONObject) json;
            try {
                castJson.getString("name");
            } catch (JSONException e) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidDescription(JSONObject obj) {
        if (obj.has("description")) {
            try {
                obj.getString("description");
                return true;
            } catch (JSONException e) {
                return false;
            }
        }
        return false;
    }

    private void setStatusInParse(Task t, JSONObject obj) {
        String status = obj.get("status").toString();
        if (status.equals("DONE")) {
            t.setStatus(Status.DONE);
        } else if (status.equals("IN_PROGRESS")) {
            t.setStatus(Status.IN_PROGRESS);
        } else if (status.equals("UP_NEXT")) {
            t.setStatus(Status.UP_NEXT);
        } else if (status.equals("TODO")) {
            t.setStatus(Status.TODO);
        }
    }

    private DueDate setDueDateInParse(JSONObject obj) {
        if (obj.get("due-date").equals(JSONObject.NULL)) {
            return null;
        } else {
            DueDate d = new DueDate();
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, obj.getJSONObject("due-date").getInt("year"));
            c.set(Calendar.MONTH, obj.getJSONObject("due-date").getInt("month"));
            c.set(Calendar.DAY_OF_MONTH, obj.getJSONObject("due-date").getInt("day"));
            c.set(Calendar.HOUR_OF_DAY, obj.getJSONObject("due-date").getInt("hour"));
            c.set(Calendar.MINUTE, obj.getJSONObject("due-date").getInt("minute"));
            d.setDueDate(c.getTime());
            return d;
        }
    }

    private void setTagsInParse(Task t, JSONObject obj) {
        JSONArray tags = obj.getJSONArray("tags");
        for (Object object : tags) {
            JSONObject tagJson = (JSONObject) object;
            t.addTag(tagJson.getString("name"));
        }
    }
    
    
}
