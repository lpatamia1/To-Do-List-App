import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    void testCompareToByPriority() {
        Task high = new Task("High", "HIGH", null);
        Task low = new Task("Low", "LOW", null);
        assertTrue(high.compareTo(low) < 0, "HIGH should come before LOW");
    }

    @Test
    void testMarkCompleted() {
        Task t = new Task("Clean desk", "MED", null);
        t.markCompleted();
        assertTrue(t.isCompleted());
    }

    @Test
    void testDueDateParsing() {
        LocalDate date = LocalDate.of(2025, 10, 9);
        Task t = new Task("Test", "LOW", date);
        assertEquals(date, t.getDue());
    }
}
