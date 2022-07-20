package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class SubTaskTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link SubTask#SubTask(String, String, TaskStatus, int)}
     *   <li>{@link SubTask#setEpicId(int)}
     *   <li>{@link SubTask#toString()}
     *   <li>{@link SubTask#getEpicId()}
     * </ul>
     */
    @Test
    void testConstructor() {
        SubTask actualSubTask = new SubTask("Dr", "The characteristics of someone or something", TaskStatus.DONE, 123);
        actualSubTask.setEpicId(123);
        String actualToStringResult = actualSubTask.toString();
        assertEquals(123, actualSubTask.getEpicId());
        assertEquals("SubTask{epicId=123}", actualToStringResult);
    }

    /**
     * Method under test: {@link SubTask#SubTask(int, String, String, int)}
     */
    @Test
    void testConstructor2() {
        SubTask actualSubTask = new SubTask(1, "Dr", "The characteristics of someone or something", 123);

        assertEquals("The characteristics of someone or something", actualSubTask.getDescription());
        assertEquals("Dr", actualSubTask.getTitle());
        assertEquals(TaskStatus.NEW, actualSubTask.getStatus());
        assertEquals(1, actualSubTask.getId());
        assertEquals(123, actualSubTask.getEpicId());
    }

    /**
     * Method under test: {@link SubTask#SubTask(String, String, int)}
     */
    @Test
    void testConstructor3() {
        SubTask actualSubTask = new SubTask("Dr", "The characteristics of someone or something", 123);

        assertEquals("The characteristics of someone or something", actualSubTask.getDescription());
        assertEquals("Dr", actualSubTask.getTitle());
        assertEquals(TaskStatus.NEW, actualSubTask.getStatus());
        assertEquals(123, actualSubTask.getEpicId());
    }

    /**
     * Method under test: {@link SubTask#equals(Object)}
     */
    @Test
    void testEquals() {
        assertNotEquals(new SubTask("Dr", "The characteristics of someone or something", 123), null);
        assertNotEquals(new SubTask("Dr", "The characteristics of someone or something", 123), "Different type to SubTask");
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link SubTask#equals(Object)}
     *   <li>{@link SubTask#hashCode()}
     * </ul>
     */
    @Test
    void testEquals2() {
        SubTask subTask = new SubTask("Dr", "The characteristics of someone or something", 123);
        assertEquals(subTask, subTask);
        int expectedHashCodeResult = subTask.hashCode();
        assertEquals(expectedHashCodeResult, subTask.hashCode());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link SubTask#equals(Object)}
     *   <li>{@link SubTask#hashCode()}
     * </ul>
     */
    @Test
    void testEquals3() {
        SubTask subTask = new SubTask("Dr", "The characteristics of someone or something", 123);
        SubTask subTask1 = new SubTask("Dr", "The characteristics of someone or something", 123);

        assertEquals(subTask, subTask1);
        int expectedHashCodeResult = subTask.hashCode();
        assertEquals(expectedHashCodeResult, subTask1.hashCode());
    }

    /**
     * Method under test: {@link SubTask#equals(Object)}
     */
    @Test
    void testEquals4() {
        SubTask subTask = new SubTask("Dr", "The characteristics of someone or something", 1);
        assertNotEquals(subTask, new SubTask("Dr", "The characteristics of someone or something", 123));
    }
}

