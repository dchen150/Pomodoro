package model;

import model.exceptions.EmptyStringException;
import model.exceptions.NullArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public class TestProject {
    Project testProject;

    @BeforeEach
    public void newProject() {
        testProject = new Project("This is a description");
    }

    @Test
    void testConstructor() {
        List<Task> testTasks = new ArrayList<Task>();
        assertEquals("This is a description", testProject.getDescription());
        //assertTrue(testTasks.equals(testProject.getTasks()));
    }

    @Test
    void testConstructorThrowEmptyStringException() {
        try {
            new Project("");
            fail("No exception was thrown");
        } catch (EmptyStringException e) {
            System.out.println("Caught expected EmptyStringException");
        }
    }
    @Test
    void testConstructorThrowEmptyStringExceptionNull() {
        try {
            new Project(null);
            fail("No exception was thrown");
        } catch (EmptyStringException e) {
            System.out.println("Caught expected EmptyStringException");
        }
    }

    @Test
    void testAddThrowNullArgumentException() {
        try {
            testProject.add(null);
            fail("No exception was thrown");
        } catch (NullArgumentException e) {
            System.out.println("Caught expected NullArgumentException");
        }
    }

    @Test
    void testRemoveThrowNullArgumentException() {
        try {
            testProject.remove(null);
            fail("No exception was thrown");
        } catch (NullArgumentException e) {
            System.out.println("Caught expected NullArgumentException");
        }
    }

    @Test
    void testAddItselfToTasks() {
        assertEquals(0, testProject.getNumberOfTasks());
        testProject.add(testProject);
        assertEquals(0, testProject.getNumberOfTasks());
    }

    @Test
    void testAddTaskThatIsNotInList() {
        assertEquals(0, testProject.getNumberOfTasks());
        testProject.add(new Task("Task1"));
        assertEquals(1, testProject.getNumberOfTasks());
    }

    @Test
    void testAddTaskThatIsAlreadyInList(){
        Task task = new Task("task 1");
        testProject.add(task);
        testProject.add(task);
        assertEquals(1, testProject.getNumberOfTasks());
    }

    @Test
    void testRemoveNotInList() {
        assertEquals(0, testProject.getNumberOfTasks());
        Task task = new Task("task 1");
        testProject.remove(task);
        assertEquals(0, testProject.getNumberOfTasks());
    }

    @Test
    void testRemoveTaskInList() {
        Task task = new Task("task 1");
        Task task2 = new Task("task 2");
        testProject.add(task);
        testProject.add(task2);
        assertEquals(2, testProject.getNumberOfTasks());
        testProject.remove(task);
        assertEquals(1, testProject.getNumberOfTasks());
        testProject.remove(task2);
        assertEquals(0, testProject.getNumberOfTasks());
    }

    @Test
    void testGetProgressNoTasks() {
        assertEquals(0, testProject.getProgress());
    }

    @Test
    void testGetProgressAllTasksCompleted() {
        Task task1 = new Task("task 1");
        Task task2 = new Task("task 2");
        task1.setProgress(100);
        task2.setProgress(100);
        testProject.add(task1);
        testProject.add(task2);
        assertEquals(100, testProject.getProgress());
    }

    @Test
    void testGetProgressHalfCompleted() {
        Task task1 = new Task("task 1");
        Task task2 = new Task("task 2");
        task1.setProgress(100);
        task2.setProgress(50);
        testProject.add(task1);
        testProject.add(task2);
        assertEquals(75, testProject.getProgress());
    }

    @Test
    void testGetProgressRoundDownToClosestInteger() {
        Task task1 = new Task("task 1");
        Task task2 = new Task("task 2");
        Task task3 = new Task("task 3");
        Task task4 = new Task("task 4");
        Task task5 = new Task("task 5");
        Task task6 = new Task("task 6");
        task1.setProgress(100);
        task2.setProgress(68);
        task3.setProgress(100);
        task4.setProgress(100);
        task5.setProgress(100);
        task6.setProgress(100);
        testProject.add(task1);
        testProject.add(task2);
        testProject.add(task3);
        testProject.add(task4);
        testProject.add(task5);
        testProject.add(task6);
        assertEquals(94, testProject.getProgress());
    }

    @Test
    void testGetProgressWithProjectAsSubProject() {
        Project project2 = new Project("proj2");
        Project project1 = new Project("proj1");
        Task task1 = new Task("task1");
        Task task2 = new Task("task2");
        Task task3 = new Task("task3");
        Task task4 = new Task("task4");

        task1.setProgress(100);
        task2.setProgress(50);
        task3.setProgress(25);

        project1.add(task1);
        project1.add(task2);
        project1.add(task3);
        project2.add(task4);
        project2.add(project1);

        assertEquals(29, project2.getProgress());


    }

    @Test
    void testGetEstimatedTimeToCompleteWithProjectAsSubProj() {
        Project project2 = new Project("proj2");
        Project project1 = new Project("proj1");
        Task task1 = new Task("task1");
        Task task2 = new Task("task2");
        Task task3 = new Task("task3");
        Task task4 = new Task("task4");

        task1.setEstimatedTimeToComplete(8);
        task2.setEstimatedTimeToComplete(2);
        task3.setEstimatedTimeToComplete(10);
        task4.setEstimatedTimeToComplete(4);

        project1.add(task1);
        project1.add(task2);
        project1.add(task3);
        project2.add(task4);
        project2.add(project1);

        assertEquals(24, project2.getEstimatedTimeToComplete());
    }

    @Test
    void testIsCompletedNoTasks() {
        assertFalse(testProject.isCompleted());
    }

    @Test
    void testIsCompletedHalfTasksCompleted() {
        Task task1 = new Task("task 1");
        Task task2 = new Task("task 2");
        task1.setProgress(100);
        task2.setProgress(50);
        testProject.add(task1);
        testProject.add(task2);
        assertFalse(testProject.isCompleted());
    }

    @Test
    void testIsCompletedAllTasksCompleted() {
        Task task1 = new Task("task 1");
        Task task2 = new Task("task 2");
        task1.setProgress(100);
        task2.setProgress(100);
        testProject.add(task1);
        testProject.add(task2);
        assertTrue(testProject.isCompleted());
    }

    @Test
    void testContains() {
        Task task1 = new Task("task 1");
        Task task2 = new Task("task 2");
        testProject.add(task2);
        assertTrue(testProject.contains(task2));
        assertFalse(testProject.contains(task1));
    }

    @Test
    void testContainsThrowNullArgumentException() {
        try {
            testProject.contains(null);
            fail("No exception thrown");
        } catch (NullArgumentException e) {
            System.out.println("Caught expected NullArgumentException");
        }
    }

    @Test
    void testProjectOverridenEquals() {
        Project testProject2 = new Project("This is a description");
        assertTrue(testProject2.equals(testProject));
        assertTrue(testProject2.hashCode() == testProject.hashCode());
        assertTrue(testProject2.equals(testProject2));
        assertFalse(testProject2.equals(new Tag("hello")));
    }

    @Test
    void testOneImpAndUrgentAndThreeDefault() {
        Task t1 = new Task("vibes");
        Task t2 = new Task("the vibes");
        Task t3 = new Task("you know");
        Project p = new Project("asdlfkjasdf");
        Task t4 = new Task("vibewers");
        Priority p1 = new Priority();
        p1.setUrgent(true);
        p1.setImportant(true);
        t4.setPriority(p1);
        p.add(t3);
        testProject.add(t4);
        testProject.add(t1);
        testProject.add(t2);
        testProject.add(p);

        ArrayList<Todo> comparisonList = new ArrayList<>();
        comparisonList.add(t4);
        comparisonList.add(t1);
        comparisonList.add(t2);
        comparisonList.add(p);

        ArrayList<Todo> iteratedList = new ArrayList<>();

        for (Todo td: testProject) {
            iteratedList.add(td);
        }
        assertTrue(iteratedList.equals(comparisonList));
    }

    @Test
    void testMultipleImpAndUrgItems() {
        Priority p = new Priority();
        p.setImportant(true);
        p.setUrgent(true);
        Task t1 = new Task("t1");
        Task t2 = new Task("t2");
        Task t3 = new Task("t3");
        Task t4 = new Task("t4");
        t1.setPriority(p);
        t3.setPriority(p);
        t2.setPriority(p);
        testProject.add(t1);
        testProject.add(t2);
        testProject.add(t4);
        testProject.add(t3);

        ArrayList<Todo> comparisonList = new ArrayList<>();
        comparisonList.add(t1);
        comparisonList.add(t2);
        comparisonList.add(t3);
        comparisonList.add(t4);

        ArrayList<Todo> iteratedList = new ArrayList<>();

        for (Todo td: testProject) {
            iteratedList.add(td);
        }
        assertTrue(iteratedList.equals(comparisonList));
    }

    @Test
    void testSomeTasksAndProjectsLoop() {
        Priority p1 = new Priority();
        p1.setImportant(true);
        p1.setUrgent(true);

        Priority p2 = new Priority();
        p2.setUrgent(true);

        Priority p3 = new Priority();
        p3.setImportant(true);

        Task t1 = new Task("t1");
        Task t2 = new Task("t2");
        Task t3 = new Task("t3");
        Task t4 = new Task("t4");
        t1.setPriority(p1);
        t2.setPriority(p2);
        t3.setPriority(p3);

        ArrayList<Todo> comparisonList = new ArrayList<>();

        comparisonList.add(t1);
        comparisonList.add(t3);
        comparisonList.add(t2);
        comparisonList.add(t4);

        testProject.add(t4);
        testProject.add(t2);
        testProject.add(t1);
        testProject.add(t3);
        ArrayList<Todo> iteratedList = new ArrayList<>();

        for (Todo td: testProject) {
            iteratedList.add(td);
        }
        assertTrue(comparisonList.equals(iteratedList));

    }
}
