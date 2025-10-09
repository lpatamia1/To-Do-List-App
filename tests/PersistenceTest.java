import org.junit.jupiter.api.Test;
import com.google.gson.*;
import java.io.*;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class PersistenceTest {

    @Test
    void testJsonSaveAndLoad() throws Exception {
        Task task = new Task("Demo", "HIGH", LocalDate.now());
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

        // Convert to JSON string
        String json = gson.toJson(task);
        Task loaded = gson.fromJson(json, Task.class);

        assertEquals(task.getName(), loaded.getName());
        assertEquals(task.getPriority(), loaded.getPriority());
    }
}
