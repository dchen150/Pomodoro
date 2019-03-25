package model;

import model.exceptions.EmptyStringException;
import model.exceptions.NullArgumentException;

import java.util.*;

// Represents a Project, a collection of zero or more Tasks
// Class Invariant: no duplicated task; order of tasks is preserved
public class Project extends Todo implements Iterable<Todo> {
    private String description;
    private List<Todo> tasks;
    
    // MODIFIES: this
    // EFFECTS: constructs a project with the given description
    //     the constructed project shall have no tasks.
    //  throws EmptyStringException if description is null or empty
    public Project(String description) {
        super(description);
        if (description == null || description.length() == 0) {
            throw new EmptyStringException("Cannot construct a project with no description");
        }
        this.description = description;
        tasks = new ArrayList<>();
    }
    
    // MODIFIES: this
    // EFFECTS: task is added to this project (if it was not already part of it)
    //   throws NullArgumentException when task is null
    public void add(Todo task) {
        if (!contains(task) && task != this) {
            tasks.add(task);
        }
    }
    
    // MODIFIES: this
    // EFFECTS: removes task from this project
    //   throws NullArgumentException when task is null
    public void remove(Todo task) {
        if (contains(task)) {
            tasks.remove(task);
        }
    }
    
    // EFFECTS: returns the description of this project
    public String getDescription() {
        return description;
    }

    @Override
    public int getEstimatedTimeToComplete() {
        int etc = 0;
        for (Todo td: tasks) {
            etc += td.getEstimatedTimeToComplete();
        }
        return etc;
    }

    // EFFECTS: returns an unmodifiable list of tasks in this project.
    @Deprecated
    public List<Task> getTasks() {
        throw new UnsupportedOperationException();
    }

    // EFFECTS: returns an integer between 0 and 100 which represents
//     the percentage of completion (rounded down to the nearest integer).
//     the value returned is the average of the percentage of completion of
//     all the tasks and sub-projects in this project.
    public int getProgress() {
        int totalProgress = 0;
        int totalTasks = 0;
        if (tasks.size() > 0) {
            for (Todo td: tasks) {
                totalProgress += td.getProgress();
                totalTasks++;
            }
            return totalProgress / totalTasks;
        } else {
            return 0;
        }
    }


    // EFFECTS: returns the number of tasks (and sub-projects) in this project
    public int getNumberOfTasks() {
        return tasks.size();
    }

    // EFFECTS: returns true if every task (and sub-project) in this project is completed, and false otherwise
//     If this project has no tasks (or sub-projects), return false.
    public boolean isCompleted() {
        return getNumberOfTasks() != 0 && getProgress() == 100;
    }
    
    // EFFECTS: returns true if this project contains the task
    //   throws NullArgumentException when task is null
    public boolean contains(Todo task) {
        if (task == null) {
            throw new NullArgumentException("Illegal argument: task is null");
        }
        return tasks.contains(task);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        Project project = (Project) o;
        return Objects.equals(description, project.description);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(description);
    }

    @Override
    public Iterator<Todo> iterator() {
        return new PrioritizedIterator();
    }

    private class PrioritizedIterator implements Iterator<Todo> {
        Iterator<Todo> ti1 = tasks.iterator();
        Iterator<Todo> ti2 = tasks.iterator();
        Iterator<Todo> ti3 = tasks.iterator();
        Iterator<Todo> ti4 = tasks.iterator();
        int numImpAndUrg = 0;
        int numImp = 0;
        int numUrg = 0;
        int numDef = 0;

        int countImpAndUrg = 0;
        int countImp = 0;
        int countUrg = 0;
        int countDef = 0;

        private PrioritizedIterator() {
            count();
        }


        @Override
        public boolean hasNext() {
            return numImpAndUrg != countImpAndUrg || numImp != countImp || numUrg != countUrg || numDef != countDef;
        }

        private void count() {
            for (int i = 0; i < tasks.size(); i++) {
                if (tasks.get(i).getPriority().isImportant() && tasks.get(i).getPriority().isUrgent()) {
                    countImpAndUrg++;
                } else if (tasks.get(i).getPriority().isImportant() && !tasks.get(i).getPriority().isUrgent()) {
                    countImp++;
                } else if (!tasks.get(i).getPriority().isImportant() && tasks.get(i).getPriority().isUrgent()) {
                    countUrg++;
                } else if (!tasks.get(i).getPriority().isImportant() && !tasks.get(i).getPriority().isUrgent()) {
                    countDef++;
                }
            }
        }


        @Override
        public Todo next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            Todo td = null;
            if (numImpAndUrg != countImpAndUrg) {
                numImpAndUrg++;
                td = findImpAndUrg();
            } else if (numImp != countImp) {
                numImp++;
                td = findImp();
            } else if (numUrg != countUrg) {
                numUrg++;
                td = findUrg();
            } else if (numDef != countDef) {
                numDef++;
                td = findDef();
            }
            return td;
        }

        private Todo findDef() {
            while (ti4.hasNext()) {
                Todo td = ti4.next();
                if (!td.getPriority().isUrgent() && !td.getPriority().isImportant()) {
                    return td;
                }
            }
            return null;
        }

        private Todo findUrg() {
            while (ti3.hasNext()) {
                Todo td = ti3.next();
                if (td.getPriority().isUrgent() && !td.getPriority().isImportant()) {
                    return td;
                }
            }
            return null;
        }

        private Todo findImp() {
            while (ti2.hasNext()) {
                Todo td = ti2.next();
                if (!td.getPriority().isUrgent() && td.getPriority().isImportant()) {
                    return td;
                }
            }
            return null;
        }

        private Todo findImpAndUrg() {
            while (ti1.hasNext()) {
                Todo td = ti1.next();
                if (td.getPriority().isUrgent() && td.getPriority().isImportant()) {
                    return td;
                }
            }
            return null;
        }


    }
}