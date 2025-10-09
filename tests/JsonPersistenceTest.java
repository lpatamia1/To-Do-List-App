import org.junit.jupiter.api.*;
import java.io.*;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class JsonPersistenceTest { // Integration test for JSON save/load functionality
// Tests that saving and loading tasks to/from JSON preserves task data
// Uses JUnit 5 for assertions and test structure

    private static final String TEST_FILE = "test_tasks.json";

    @Test
    void testSaveAndLoadPreservesTasks() throws IOException {
        // Create test tasks
        List<Task> testTasks = List.of(
            new Task("A", "HIGH", LocalDate.of(2025, 10, 10)),
            new Task("B", "LOW", null)
        );

        // Save to file using Gson (like todo does)
        try (Writer writer = new FileWriter(TEST_FILE)) {
            com.google.gson.Gson gson = new com.google.gson.GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();
            gson.toJson(testTasks, writer);
        }

        // Load back
        List<Task> reloaded;
        try (Reader reader = new FileReader(TEST_FILE)) {
            com.google.gson.Gson gson = new com.google.gson.GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
            Task[] arr = gson.fromJson(reader, Task[].class);
            reloaded = Arrays.asList(arr);
        }

        // Assertions
        assertEquals(2, reloaded.size());
        assertEquals("A", reloaded.get(0).getName());
        assertEquals("LOW", reloaded.get(1).getPriority());

        // Cleanup
        new File(TEST_FILE).delete();
    }
}
