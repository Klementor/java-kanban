package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class TaskTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link Task#Task(int, String, String, TaskStatus)}
     *   <li>{@link Task#setDescription(String)}
     *   <li>{@link Task#setId(int)}
     *   <li>{@link Task#setStatus(TaskStatus)}
     *   <li>{@link Task#setTitle(String)}
     *   <li>{@link Task#toString()}
     *   <li>{@link Task#getDescription()}
     *   <li>{@link Task#getId()}
     *   <li>{@link Task#getStatus()}
     *   <li>{@link Task#getTitle()}
     * </ul>
     */
    @Test
    void testConstructor() {
        Task actualTask = new Task(1, "Dr", "The characteristics of someone or something", TaskStatus.DONE);
        actualTask.setDescription("The characteristics of someone or something");
        actualTask.setId(1);
        actualTask.setStatus(TaskStatus.DONE);
        actualTask.setTitle("Dr");
        String actualToStringResult = actualTask.toString();
        assertEquals("The characteristics of someone or something", actualTask.getDescription());
        assertEquals(1, actualTask.getId());
        assertEquals(TaskStatus.DONE, actualTask.getStatus());
        assertEquals("Dr", actualTask.getTitle());
        assertEquals("Task{title='Dr', description='The characteristics of someone or something', id=1, status=DONE, startTime=null, duration=null}",
                actualToStringResult);
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link Task#Task(String, String, TaskStatus)}
     *   <li>{@link Task#setDescription(String)}
     *   <li>{@link Task#setId(int)}
     *   <li>{@link Task#setStatus(TaskStatus)}
     *   <li>{@link Task#setTitle(String)}
     *   <li>{@link Task#toString()}
     *   <li>{@link Task#getDescription()}
     *   <li>{@link Task#getId()}
     *   <li>{@link Task#getStatus()}
     *   <li>{@link Task#getTitle()}
     * </ul>
     */
    @Test
    void testConstructor2() {
        Task actualTask = new Task("Dr", "The characteristics of someone or something", TaskStatus.DONE);
        actualTask.setDescription("The characteristics of someone or something");
        actualTask.setId(1);
        actualTask.setStatus(TaskStatus.DONE);
        actualTask.setTitle("Dr");
        String actualToStringResult = actualTask.toString();
        assertEquals("The characteristics of someone or something", actualTask.getDescription());
        assertEquals(1, actualTask.getId());
        assertEquals(TaskStatus.DONE, actualTask.getStatus());
        assertEquals("Dr", actualTask.getTitle());
        assertEquals("Task{title='Dr', description='The characteristics of someone or something', id=1, status=DONE, startTime=null, duration=null}",
                actualToStringResult);
    }

    /**
     * Method under test: {@link Task#Task(String, String)}
     */
    @Test
    void testConstructor3() {
        Task actualTask = new Task("Dr", "The characteristics of someone or something");

        assertEquals("The characteristics of someone or something", actualTask.getDescription());
        assertEquals("Dr", actualTask.getTitle());
        assertEquals(TaskStatus.NEW, actualTask.getStatus());
    }

    /**
     * Method under test: {@link Task#equals(Object)}
     */
    @Test
    void testEquals() {
        assertNotEquals(new Task("Dr", "The characteristics of someone or something"), null);
        assertNotEquals(new Task("Dr", "The characteristics of someone or something"), "Different type to Task");
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link Task#equals(Object)}
     *   <li>{@link Task#hashCode()}
     * </ul>
     */
    @Test
    void testEquals2() {
        Task task = new Task("Dr", "The characteristics of someone or something");
        assertEquals(task, task);
        int expectedHashCodeResult = task.hashCode();
        assertEquals(expectedHashCodeResult, task.hashCode());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link Task#equals(Object)}
     *   <li>{@link Task#hashCode()}
     * </ul>
     */
    @Test
    void testEquals3() {
        Task task = new Task("Dr", "The characteristics of someone or something");
        Task task1 = new Task("Dr", "The characteristics of someone or something");

        assertEquals(task, task1);
        int expectedHashCodeResult = task.hashCode();
        assertEquals(expectedHashCodeResult, task1.hashCode());
    }

    /**
     * Method under test: {@link Task#equals(Object)}
     */
    @Test
    void testEquals4() {
        Task task = new Task("Title", "The characteristics of someone or something");
        assertNotEquals(task, new Task("Dr", "The characteristics of someone or something"));
    }

    /**
     * Method under test: {@link Task#equals(Object)}
     */
    @Test
    void testEquals5() {
        Task task = new Task("Dr", "Description");
        assertNotEquals(task, new Task("Dr", "The characteristics of someone or something"));
    }

    /**
     * Method under test: {@link Task#equals(Object)}
     */
    @Test
    void testEquals6() {
        Task task = new Task("Dr", "The characteristics of someone or something", TaskStatus.DONE);
        assertNotEquals(task, new Task("Dr", "The characteristics of someone or something"));
    }

    /**
     * Method under test: {@link Task#equals(Object)}
     */
    @Test
    void testEquals7() {
        Task task = new Task(1, "Dr", "The characteristics of someone or something", TaskStatus.DONE);
        assertNotEquals(task, new Task("Dr", "The characteristics of someone or something"));
    }
}

