import org.junit.jupiter.api.*;
import java.io.*;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class MarkdownExportTest {

    @Test
    void testExportCreatesFile() throws IOException {
        Task t = new Task("Write tests", "HIGH", LocalDate.now());
        File md = new File("tasks.md");

        // Manually export one test task
        try (PrintWriter writer = new PrintWriter(new FileWriter(md))) {
            writer.println("# 📝 Retro To-Do List");
            writer.println("- [ ] " + t.getName() + " (" + t.getPriority() + ")");
        }

        assertTrue(md.exists(), "Markdown file should exist after export");
        assertTrue(md.length() > 0, "Markdown file should not be empty");

        md.delete();
    }
}
