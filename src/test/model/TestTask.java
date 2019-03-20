package model;

import model.exceptions.EmptyStringException;
import model.exceptions.InvalidProgressException;
import model.exceptions.NegativeInputException;
import model.exceptions.NullArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sun.invoke.empty.Empty;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TestTask {
    private Task testTask;

    @BeforeEach
    public void newTask() {
        testTask = new Task("Hello, this is a description");
    }

    @Test
    void testConstructorSetDescriptionParse() {
        testTask.setDescription("Register for the course. ## cpsc210; tomorrow; important; urgent; in progress");
        assertEquals("\n{\n" + "\tDescription: Register for the course. \n"
                + "\tDue date: " + testTask.getDueDate() + "\n"
                + "\tStatus: IN PROGRESS\n"
                + "\tPriority: IMPORTANT & URGENT\n"
                + "\tTags: #cpsc210\n}", testTask.toString());
        assertEquals("Register for the course. ", testTask.getDescription());
        assertEquals(Status.IN_PROGRESS, testTask.getStatus());
    }

    @Test
    void testConstsructorEmptyStringException() {
        try {
            Task t = new Task(null);
            fail("Expected exception not thrown");
        } catch (EmptyStringException e) {
            System.out.println("Expected exception thrown");
        }
        try {
            Task t1 = new Task("");
            fail("Expected exception not thrown");
        } catch (EmptyStringException e) {
            System.out.println("Expected exception thrown");
        }
    }

    @Test
    void testConstructorParse2() {
        Task parseTask = new Task("task3 ## important; tag3; important");
        assertEquals("\n{\n" + "\tDescription: task3 \n"
                + "\tDue date: " + "\n"
                + "\tStatus: TODO\n"
                + "\tPriority: IMPORTANT\n"
                + "\tTags: #tag3\n}", parseTask.toString());
    }

//    @Test
//    void testSetDescriptionParse() {
//        testTask.setDescription("Register for the course.##");
//        assertEquals("Register for the course.", testTask.getDescription());
//        assertEquals(0, testTask.getTags().size());
//    }

    @Test
    void testContainsTagThrowEmptyStringException() {
        try {
            testTask.containsTag("");
            fail("Exception not thrown");
        } catch (EmptyStringException e) {
            System.out.println("Expected EmptyStringException caught");
        }
    }

    @Test
    void testContainsTagThrowEmptyStringExceptionOverloaded() {
        try {
            Tag tag = null;
            testTask.containsTag(tag);
            fail("Exception not thrown");
        } catch (NullArgumentException e) {
            System.out.println("Expected NullArgumentException caught");
        }
    }

    @Test
    void testSetDescriptionAddOneTag() {
        testTask.setDescription("task1## ;");
        assertEquals("task1", testTask.getDescription());
        assertEquals(0, testTask.getTags().size());
    }

    @Test
    void testSetDescriptionWithLotsOfSpaces() {
        testTask.setDescription("    ## hello");
        assertEquals("    ", testTask.getDescription());
        assertTrue(testTask.containsTag("hello"));
    }


    @Test
    void testConstructorWithNoMetaData() {
        Set<Tag> testTags = new LinkedHashSet<Tag>();
        assertEquals("Hello, this is a description", testTask.getDescription());
        assertEquals(null, testTask.getDueDate());
        assertEquals(Status.TODO, testTask.getStatus());
        assertFalse(testTask.getPriority().isUrgent());
        assertFalse(testTask.getPriority().isImportant());
        assertTrue(testTags.equals(testTask.getTags()));
    }

    @Test
    void testConstructorDoesNotThrowEmptyStringException() {
        try {
            new Task("hello world");
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException caught");
        }
    }

    @Test
    void testConstructorThrowsEmptyStringException() {
        try {
            new Task("");
        } catch (EmptyStringException e) {
            System.out.println("Expected EmptyStringException caught");
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException caught");
        }
    }

    @Test
    void testAddTagNotAlreadyInTags() {
        boolean successfullyAdded = false;
        try {
            testTask.addTag("TestTag");
            for (Tag t : testTask.getTags()) {
                if (t.getName() == "TestTag") {
                    successfullyAdded = true;
                }
            }
            assertTrue(successfullyAdded);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException caught");
        }
    }

    @Test
    void testAddTagAlreadyInTags() {
        int count = 0;
        boolean successfullyAdded = false;
        try {
            testTask.addTag("TestTag");
            testTask.addTag("TestTag");
            for (Tag t : testTask.getTags()) {
                if (t.getName() == "TestTag") {
                    count += 1;
                    successfullyAdded = true;
                }
            }
            assertEquals(1, count);
            assertTrue(successfullyAdded);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException caught");
        }
    }

    @Test
    void testAddTagNoExceptionThrown() {
        try {
            testTask.addTag("");
        } catch (EmptyStringException e) {
            System.out.println("Expected EmptyStringException caught");
        }
    }

    @Test
    void testRemoveTagNotInSet() {
        testTask.addTag("TestTag1");
        testTask.addTag("TestTag2");
        testTask.addTag("TestTag3");
        try {
            testTask.removeTag("TestTag4");
            assertEquals(3, testTask.getTags().size());
        } catch (EmptyStringException e) {
            fail("Unexpected EmptyStringException caught");
        }
    }

    @Test
    void testRemoveTagThatIsInSet() {
        testTask.addTag("TestTag1");
        testTask.addTag("TestTag2");
        testTask.addTag("TestTag3");
        try {
            testTask.removeTag("TestTag3");
            assertFalse(testTask.containsTag("TestTag3"));
            assertEquals(2, testTask.getTags().size());
        } catch (EmptyStringException e) {
            fail("Unexpected EmptyStringException caught");
        }
    }

    @Test
    void testRemoveTagMoreThanOnce() {
        testTask.addTag("TestTag1");
        testTask.addTag("TestTag2");
        testTask.addTag("TestTag3");
        try {
            testTask.removeTag("TestTag3");
            testTask.removeTag("TestTag3");
            testTask.removeTag("TestTag3");
            testTask.removeTag("TestTag3");
            testTask.removeTag("TestTag3");
            assertFalse(testTask.containsTag("TestTag3"));
            assertEquals(2, testTask.getTags().size());
        } catch (EmptyStringException e) {
            fail("Unexpected EmptyStringException caught");
        }

    }

    @Test
    void testRemoveMultipleTags() {
        testTask.addTag("TestTag1");
        testTask.addTag("TestTag2");
        testTask.addTag("TestTag3");
        try {
            testTask.removeTag("TestTag3");
        } catch (EmptyStringException e) {
            fail("Unexpected EmptyStringException caught");
        }
        try {
            testTask.removeTag("TestTag2");
            assertFalse(testTask.containsTag("TestTag3"));
            assertFalse(testTask.containsTag("TestTag2"));
            assertEquals(1, testTask.getTags().size());
        } catch (EmptyStringException e) {
            fail("Unexpected EmptyStringException caught");
        }
    }

    @Test
    void testRemoveTagThrowEmptyStringException() {
        try {
            testTask.removeTag("");
        } catch (EmptyStringException e) {
            System.out.println("Expected EmptyStringException ");
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException caught");
        }
    }

    @Test
    void testSetPriority() {
        Priority testPriority = new Priority();
        testPriority.setUrgent(true);
        testPriority.setImportant(true);
        try {
            testTask.setPriority(testPriority);
        } catch (NullArgumentException e) {
            fail("Unexpected NullArgumentException caught");
        }
        assertTrue(testTask.getPriority().isUrgent());
        assertTrue(testTask.getPriority().isImportant());
    }

    @Test
    void testSetPriorityThrowException() {
        try {
            testTask.setPriority(null);
        } catch (NullArgumentException e) {
            System.out.println("Expected NullArgumentException caught");
        }
    }

    @Test
    void testSetDescription() {
        try {
            testTask.setDescription("new description");
            assertEquals("new description", testTask.getDescription());
        } catch (EmptyStringException e) {
            fail("Unexpected EmptyStringException caught");
        }
    }

    @Test
    void testSetStatus() {
        testTask.setStatus(Status.TODO);
        assertTrue(testTask.getStatus().equals(Status.TODO));
    }

    @Test
    void testSetStatusThrowNullArgumentException() {
        try {
            testTask.setStatus(null);
            fail("No exception was thrown");
        } catch (NullArgumentException e) {
            System.out.println("Caught expected NullArgumentException");
        }
    }

    @Test
    void testSetDescriptionThrowEmptyStringException() {
        try {
            testTask.setDescription("");
            fail("Expected exception not thrown");
        } catch (EmptyStringException e) {
            System.out.println("Caught expected EmptyStringException");
        }
    }

    @Test
    void testSetDescriptionThrowEmptyStringExceptionNull() {
        try {
            testTask.setDescription(null);
            fail("Expected exception not thrown");
        } catch (EmptyStringException e) {
            System.out.println("Caught expected EmptyStringException");
        }
    }

    @Test
    void testToString(){
        DueDate testDueDate = new DueDate();
        testTask.setDueDate(testDueDate);
        testTask.addTag("TestTag1");
        testTask.addTag("TestTag2");
        testTask.addTag("TestTag3");
        String expectedString = "\n{\n\tDescription: Hello, this is a description\n" +
                "\tDue date: " + testTask.getDueDate().toString() + "\n\tStatus: TODO\n" +
                "\tPriority: DEFAULT\n\tTags: #TestTag3, #TestTag2, #TestTag1\n}";
        assertEquals(expectedString, testTask.toString());
    }

    @Test
    void testTaskOverridenEquals() {
        Task testTask2 = new Task("Hello, this is a description");
        assertTrue(testTask2.equals(testTask));
        assertTrue(testTask2.equals(testTask2));
        assertFalse(testTask2.equals(new Tag("hello")));
    }

    @Test
    void testTaskOverridenEqualsPart2() {
        Task testTask2 = new Task("Hello, this is a description");
        DueDate dueDate = new DueDate();
        Priority p = new Priority();
        p.setUrgent(true);

        testTask2.setDueDate(dueDate);
        testTask2.setPriority(p);
        assertFalse(testTask2.equals(testTask));
        assertTrue(testTask2.equals(testTask2));
        assertFalse(testTask2.equals(new Tag("hello")));

        testTask.setPriority(p);
        testTask.setDueDate(dueDate);
        assertTrue(testTask2.equals(testTask));
    }

    @Test
    void testToStringNoTags(){
        DueDate testDueDate = new DueDate();
        testTask.setDueDate(testDueDate);
        String expectedString = "\n{\n\tDescription: Hello, this is a description\n" +
                "\tDue date: " + testTask.getDueDate().toString() + "\n\tStatus: TODO\n" +
                "\tPriority: DEFAULT\n\tTags:  " + "\n}";
        assertEquals(expectedString, testTask.toString());
    }

    @Test
    void testGetProgressZeroAndETCZero() {
        assertTrue(testTask.getProgress() == 0);
        assertTrue(testTask.getEstimatedTimeToComplete() == 0);
    }

    @Test
    void testSetEstimatedTimeToComplete() {
        testTask.setEstimatedTimeToComplete(23);
        assertEquals(23, testTask.getEstimatedTimeToComplete());

        testTask.setProgress(34);
        assertEquals(34, testTask.getProgress());
    }

    @Test
    void testNegativeInputException() {
        try {
            testTask.setEstimatedTimeToComplete(-1);
            fail("Expected exception not thrown");
        } catch (NegativeInputException e) {
            System.out.println("Expected exception caught");
        }

        try {
            testTask.setProgress(-1);
            fail("Expected exception not thrown");
        } catch (InvalidProgressException e) {
            System.out.println("Expected exception caught");
        }

        try {
            testTask.setProgress(0);
        } catch (InvalidProgressException e) {
            fail("Expected exception not thrown");
        }

        try {
            testTask.setProgress(100);
        } catch (InvalidProgressException e) {
            fail("Expected exception not thrown");
        }

        try {
            testTask.setProgress(101);
            fail("Expected exception not thrown");
        } catch (InvalidProgressException e) {
            System.out.println("Expected exception caught");
        }
    }

}
