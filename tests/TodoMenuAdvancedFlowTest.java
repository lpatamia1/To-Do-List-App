import org.junit.jupiter.api.*;
import java.io.*;
import java.time.LocalDate;

public class TodoMenuAdvancedFlowTest {

    @Test
    void testAddCompleteAndExportFlow() throws Exception {
        // Simulate user input for: add -> complete -> export -> exit
        String simulatedInput = String.join(System.lineSeparator(),
            "1",                        // Add a task
            "Automated test task",      // Task name
            "M",                        // Priority
            LocalDate.now().plusDays(1).toString(), // Due tomorrow
            "",                         // Press enter to return
            "3",                        // Mark complete
            "1",                        // Complete first task
            "",                         // Press enter to return
            "5",                        // Export markdown
            "",                         // Press enter to return
            "7",                         // Exit
            ""
        ) + System.lineSeparator();

        // Redirect System.in/out
        InputStream testInput = new ByteArrayInputStream(simulatedInput.getBytes());
        ByteArrayOutputStream testOutput = new ByteArrayOutputStream();

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        System.setIn(testInput);
        System.setOut(new PrintStream(testOutput));

        todo.main(new String[]{});

        // Restore
        System.setIn(originalIn);
        System.setOut(originalOut);

        // Verify output
        String consoleOutput = testOutput.toString();
        Assertions.assertTrue(consoleOutput.contains("✅ Completed"), "Should confirm completion");
        Assertions.assertTrue(consoleOutput.contains("🗒️"), "Should export Markdown");
        Assertions.assertTrue(consoleOutput.contains("Goodbye"), "Should exit cleanly");

        // Verify Markdown file exists
        File md = new File("tasks.md");
        Assertions.assertTrue(md.exists(), "Markdown export file should be created");
        Assertions.assertTrue(md.length() > 10, "Markdown file should not be empty");
        md.delete(); // Clean up
    }
}
