import org.junit.jupiter.api.*;
import java.io.*;
import java.time.LocalDate;

public class TodoMenuFlowTest { // Integration test for main menu flow in todo.main
// Simulates user input to add a task and then exit
// Verifies console output for expected messages
// Uses JUnit 5 for assertions and test structure

    @Test
    void testMenuAddAndExitFlow() throws Exception {
        // Simulate user input for: add -> exit
        String simulatedInput = String.join(System.lineSeparator(),
            "1",                    // Add task
            "Test from automation", // Task name
            "H",                    // High priority
            LocalDate.now().toString(), // Due date
            "",                     // Press enter to return
            "7"                     // Exit
        ) + System.lineSeparator();

        InputStream testInput = new ByteArrayInputStream(simulatedInput.getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        PrintStream originalOut = System.out;
        InputStream originalIn = System.in;
        System.setIn(testInput);
        System.setOut(new PrintStream(output));

        // Run app (this will auto exit after simulated input)
        todo.main(new String[]{});

        System.setIn(originalIn);
        System.setOut(originalOut);

        String consoleOutput = output.toString();

        Assertions.assertTrue(consoleOutput.contains("🌸 Retro To-Do List 🌸"));
        Assertions.assertTrue(consoleOutput.contains("Task added!"));
        Assertions.assertTrue(consoleOutput.contains("Goodbye"), "Should print exit message");
    }
}
