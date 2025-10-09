import org.junit.jupiter.api.Test;
import com.google.gson.*;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class LocalDateAdapterTest {

    @Test
    void testSerializeAndDeserialize() {
        LocalDateAdapter adapter = new LocalDateAdapter();
        LocalDate original = LocalDate.of(2025, 10, 9);
        JsonElement json = adapter.serialize(original, null, null);
        LocalDate parsed = adapter.deserialize(json, null, null);
        assertEquals(original, parsed, "Serialization + deserialization should match");
    }
}
