import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.time.LocalDate;
import com.google.gson.*;

public class todo {
    private static final List<Task> tasks = new ArrayList<>();
    private static final SimpleDateFormat timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static int addedToday = 0;
    private static int completedToday = 0;
    private static final java.time.LocalDateTime startTime = java.time.LocalDateTime.now();

    public static void main(String[] args) {
        retroBoot();
        loadTasksJSON();
        showStartupSummary();
        Scanner scanner = new Scanner(System.in);
        ColorText.banner("      🌸 Retro To-Do List 🌸");
        showQuote();

        while (true) {
            ColorText.line();
            System.out.println("1️⃣  Add Task\n2️⃣  View Tasks\n3️⃣  Mark Complete\n4️⃣  Show Upcoming\n5️⃣  Export Markdown\n6️⃣  View Completed\n7️⃣  Exit");
            System.out.print(ColorText.CYAN + "Choose: " + ColorText.RESET);
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": addTask(scanner); break;
                case "2": showTasks(); break;
                case "3": completeTask(scanner); break;
                case "4": showUpcoming(); break;
                case "5": exportMarkdown(); break;
                case "6": showCompleted(); break;
                case "7": exitApp(); scanner.close(); return;
                default: beep(); ColorText.warn("❓ Invalid option. Try again!");
            }
        }
    }

    // --- Core Features ---

    private static void addTask(Scanner scanner) {
        System.out.print(ColorText.YELLOW + "Enter task: " + ColorText.RESET);
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            ColorText.warn("You can’t add an empty task!");
            return;
        }

        System.out.print(ColorText.YELLOW + "Set priority (H/M/L or blank): " + ColorText.RESET);
        String input = scanner.nextLine().trim().toUpperCase();
        String priority;
        switch (input) {
            case "H": priority = "HIGH"; break;
            case "M": priority = "MED"; break;
            case "L": priority = "LOW"; break;
            default: priority = "NONE"; break;
        }

        System.out.print(ColorText.YELLOW + "Enter due date (YYYY-MM-DD or blank): " + ColorText.RESET);
        String dueInput = scanner.nextLine().trim();
        LocalDate due = null;
        if (!dueInput.isEmpty()) {
            try { due = LocalDate.parse(dueInput); }
            catch (Exception e) { ColorText.warn("⚠️ Invalid date format. Skipping due date."); }
        }

        Task newTask = new Task(name, priority, due);
        tasks.add(newTask);
        addedToday++;
        Collections.sort(tasks);
        beep();
        ColorText.success("Task added!");
        saveTasksJSON();
        pauseAndClear(scanner);
    }

    private static void showTasks() {
        Collections.sort(tasks);
        if (tasks.isEmpty()) {
            ColorText.warn("🌈 No tasks yet!");
            pauseAndClear(new Scanner(System.in));
            return;
        }

        // --- Table Header (your formatting kept) ---
        ColorText.info("\n📝 Your Tasks:");
        System.out.printf(ColorText.CYAN + "%-3s %-37s %-18s %-12s%n" + ColorText.RESET,
                "#", "Task", "Priority", "Due Date");
        System.out.println(ColorText.CYAN + "───────────────────────────────────────────────────────────────────────" + ColorText.RESET);

        // --- Table Rows ---
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            if (t.isCompleted()) continue;

            String priority = t.getPriority();
            String due = (t.getDue() == null) ? "💤 [NO DUE DATE]" : t.getDue().toString();

            // --- Color priority like your version ---
            String coloredPriority;
            switch (priority) {
                case "HIGH":
                    coloredPriority = ColorText.RED + "🔥 [HIGH]" + ColorText.RESET;
                    break;
                case "MED":
                    coloredPriority = ColorText.YELLOW + "⚡ [MED]" + ColorText.RESET;
                    break;
                case "LOW":
                    coloredPriority = ColorText.GREEN + "🌿 [LOW]" + ColorText.RESET;
                    break;
                default:
                    coloredPriority = ColorText.GRAY + "💤 [NONE]" + ColorText.RESET;
                    break;
            }

            // --- Highlight due date ---
            if (t.getDue() != null) {
                LocalDate d = t.getDue();
                if (LocalDate.now().isAfter(d)) due = ColorText.RED + due + " ⚠️" + ColorText.RESET;
                else if (LocalDate.now().equals(d)) due = ColorText.YELLOW + due + " ⏰" + ColorText.RESET;
                else due = ColorText.GREEN + due + ColorText.RESET;
            }

            // --- Print row ---
            System.out.printf(ColorText.BLUE + "%-3d %-37s %-26s %-12s" + ColorText.RESET + "%n",
                    (i + 1), t.getName(), coloredPriority, due);
        }

        long noDate = tasks.stream().filter(t -> t.getDue() == null).count();
        ColorText.info(String.format("\n📊 %d total | %d without due date | %d added today | %d completed today",
            tasks.size(), noDate, addedToday, completedToday));

        pauseAndClear(new Scanner(System.in));
    }

    private static void completeTask(Scanner scanner) {
        showTasks();
        if (tasks.isEmpty()) return;

        System.out.print(ColorText.YELLOW + "Enter number to complete: " + ColorText.RESET);
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index >= 0 && index < tasks.size()) {
                Task t = tasks.get(index);
                t.markCompleted();
                completedToday++;
                beep();
                ColorText.success("✅ Completed: " + t.getName());
                saveTasksJSON();
            } else {
                beep();
                ColorText.warn("Invalid task number!");
            }
        } catch (NumberFormatException e) {
            beep();
            ColorText.warn("Please enter a valid number.");
        }
        pauseAndClear(scanner);
    }

    private static void showCompleted() {
        ColorText.info("\n✅ Completed Tasks:");
        boolean found = false;

        for (Task t : tasks) {
            if (t.isCompleted()) {
                System.out.println(ColorText.GREEN + "✔️  " + t.getName() + ColorText.RESET);
                found = true;
            }
        }

        if (!found)
            System.out.println(ColorText.YELLOW + "No completed tasks yet!" + ColorText.RESET);

        pauseAndClear(new Scanner(System.in));
    }

    private static void showUpcoming() {
        ColorText.info("\n📅 Tasks due within 3 days:");
        boolean found = false;

        for (Task t : tasks) {
            LocalDate due = t.getDue();
            if (due != null && !LocalDate.now().isAfter(due) && !due.isAfter(LocalDate.now().plusDays(3))) {

                String emoji;
                if (due.equals(LocalDate.now()))
                    emoji = ColorText.RED + "⚠️  " + ColorText.RESET; // Due today
                else if (due.equals(LocalDate.now().plusDays(1)))
                    emoji = ColorText.YELLOW + "⏰ " + ColorText.RESET; // Due tomorrow
                else
                    emoji = ColorText.GREEN + "🌿 " + ColorText.RESET; // Due in 2-3 days

                // ✅ Now prints correctly, emoji appears before the task name
                System.out.println(emoji + t.getName() + " → " + ColorText.CYAN + due + ColorText.RESET);
                found = true;
            }
        }

        if (!found)
            System.out.println(ColorText.GREEN + "No tasks due soon!" + ColorText.RESET);

        pauseAndClear(new Scanner(System.in));
    }

    private static void exportMarkdown() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("tasks.md"))) {
            writer.println("# 📝 Retro To-Do List");
            writer.println("_Updated: " + timestamp.format(new Date()) + "_\n");
            if (tasks.isEmpty()) {
                writer.println("✅ All tasks completed! 🎉");
            } else {
                writer.println("## Pending Tasks:\n");
                for (Task t : tasks) {
                    String box = t.isCompleted() ? "[x]" : "[ ]";
                    writer.println("- " + box + " " + t.getName() + " (" + t.getPriority() + ")");
                }
            }
            ColorText.success("🗒️  Exported to tasks.md");
        } catch (IOException e) {
            ColorText.warn("⚠️ Could not export to Markdown.");
        }
    }

    // --- Aesthetic Touches ---

    private static void retroBoot() {
        String[] bootMsgs = {
            "Spinning floppy drive... 💾",
            "Installing Java Runtime... ☕",
            "Rendering pixels of productivity... 🎨",
            "Charging up your to-do list... ⚡",
            "Calibrating retro vibes... 🕹️",
            "Checking for Y2K bugs... 🧮",
            "Loading system aesthetics... 🌈",
            "Compiling positive energy... ✨",
            "Booting motivation module... 🚀"
        };

        List<String> shuffled = new ArrayList<>(Arrays.asList(bootMsgs));
        Collections.shuffle(shuffled);
        List<String> chosen = shuffled.subList(0, 4);

        System.out.println(ColorText.BLACK + "🔧 Initializing Retro Environment..." + ColorText.RESET);

        int totalSteps = 40;
        for (int i = 0; i <= totalSteps; i++) {
            int progress = (i * 100) / totalSteps;

            StringBuilder bar = new StringBuilder();
            for (int j = 0; j < i; j++) {
                double ratio = (double) j / totalSteps;

                // 🌈 Proper red-start rainbow gradient using rotated sine waves
                double frequency = 1.7 * Math.PI;

                // 🌈 Correct alignment: starts at pure red
                int r = (int)(Math.sin(frequency * ratio + Math.PI / 2) * 127 + 128);         // red peaks first
                int g = (int)(Math.sin(frequency * ratio - Math.PI / 6) * 127 + 128);         // green lags slightly
                int b = (int)(Math.sin(frequency * ratio - 5 * Math.PI / 6) * 127 + 128);     // blue trails farthest

                bar.append(String.format("\033[38;2;%d;%d;%dm█\033[0m", r, g, b));
            }
            for (int j = i; j < totalSteps; j++) bar.append("▒");

            System.out.printf("\rLoading: [%s] %3d%%", bar, progress);
            try { Thread.sleep(70); } catch (InterruptedException ignored) {}
        }
        System.out.println("\033[0m"); // reset color

        System.out.println();
        for (String msg : chosen) {
            System.out.println(ColorText.BLACK + msg + ColorText.RESET);
            try { Thread.sleep(600); } catch (InterruptedException ignored) {}
        }
        System.out.println("\n" + ColorText.CYAN + "✨ Ready to roll! ✨" + ColorText.RESET);
    }

    private static void showQuote() {
        String[] quotes = {
            "Keep calm and code on 💻",
            "Progress, not perfection 🌈",
            "Every bug teaches you something 🐛",
            "One task at a time 🪩",
            "Stay groovy and productive! 🎸",
            "You are doing great! 🌟",
            "Make today ridiculously amazing! 🚀",
        };
        Random rand = new Random();
        System.out.println(ColorText.CYAN + "💬 " + quotes[rand.nextInt(quotes.length)] + ColorText.RESET);
    }

    private static void exitApp() {
        ColorText.line();
        ColorText.success("Exiting… your tasks are saved!");
        ColorText.info("📅 Today’s Stats: Added " + addedToday + " | Completed " + completedToday);
        
        saveTasksJSON();
        exportMarkdown();

        ColorText.info("📂 Serialized " + tasks.size() + " tasks to tasks.json");
    
        java.time.Duration session = java.time.Duration.between(startTime, java.time.LocalDateTime.now());
        long minutes = session.toMinutes();
        long seconds = session.minusMinutes(minutes).getSeconds();

        if (minutes == 0 && seconds < 10) {
            ColorText.info("⏱️  Session Duration: " + seconds + " seconds");
        }
        else {
            ColorText.info(String.format("⏱️  Session Duration: %d minutes, %d seconds", minutes, seconds));
        }
        
        ColorText.success("✨ Goodbye! Stay groovy and productive! 🎸");

        try {
            Thread.sleep(400);
            System.out.print("\n" + ColorText.PINK + "             •beep•" + ColorText.RESET + "\n");
            java.awt.Toolkit.getDefaultToolkit().beep();
            System.out.flush();
            Thread.sleep(600);
            fadeOut();
        } catch (InterruptedException ignored) {}
    }

    private static void beep() {
        try {
            // Try real beep if possible
            java.awt.Toolkit.getDefaultToolkit().beep();
        } catch (Exception ignored) {}

        // Retro visual beep — centered on its own line
        System.out.print("\n" + ColorText.PINK + "             •beep•" + ColorText.RESET + "\n");
        System.out.flush();

        // Little blink animation to simulate feedback
        try {
            Thread.sleep(120);
            System.out.print("\r" + "             " + "\r");
            Thread.sleep(100);
            System.out.print(ColorText.PINK + "             •beep•" + ColorText.RESET + "\n");
            System.out.flush();
        } catch (InterruptedException ignored) {}
    }

    private static void fadeOut() {
        String[] frames = {
            ColorText.PINK + "▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒" + ColorText.RESET,
            ColorText.PURPLE + "░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░" + ColorText.RESET,
            ColorText.GRAY + "                                " + ColorText.RESET
        };

        for (String frame: frames) {
            System.out.print("\r" + frame);
            System.out.flush();
            try { Thread.sleep(90); } catch (InterruptedException ignored) {}
        }

        System.out.print("\r" + "                             " + "\r"); 
        System.out.println();
    }

    // --- Utility Helpers ---

    private static void pauseAndClear(Scanner scanner) {
        System.out.println("\nPress Enter to return to menu...");
        if (scanner.hasNextLine()) scanner.nextLine();
        clearScreen();
    }


    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // --- JSON Persistence ---

    private static final String JSON_FILE = "tasks.json";

    /** Save all tasks to a JSON file */
    private static void saveTasksJSON() {
        try (Writer writer = new FileWriter(JSON_FILE)) {
            Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();
            gson.toJson(tasks, writer);
            ColorText.info("Tasks saved to tasks.json");
        } catch (IOException e) {
            ColorText.warn("⚠️ Could not save tasks.json: " + e.getMessage());
        }
    }

    private static void loadTasksJSON() {
        File file = new File(JSON_FILE);
        if (!file.exists()) return;
        try (Reader reader = new FileReader(file)) {
            Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();
            Task[] loaded = gson.fromJson(reader, Task[].class);
            if (loaded != null) tasks.addAll(Arrays.asList(loaded));
            ColorText.success("📂 Loaded " + tasks.size() + " task(s) from tasks.json");
        } catch (Exception e) {
            ColorText.warn("⚠️ Could not load tasks.json: " + e.getMessage());
        }
    }
    private static void showStartupSummary() {
        if (tasks.isEmpty()) {
            ColorText.info("\n📭 No tasks yet. Let's add your first one!");
            return;
        }

        long totalActive = TodoLogic.countActive(tasks);
        long dueSoon = TodoLogic.countDueSoon(tasks);
        long overdue = TodoLogic.countOverdue(tasks);

        ColorText.line();
        ColorText.info(String.format(
            "📅 You have %d active task%s (%d due soon, %d overdue)",
            totalActive,
            totalActive == 1 ? "" : "s",
            dueSoon,
            overdue
        ));

        // Optional: motivational touch
        if (overdue > 0)
            ColorText.warn("⚠️  Time to catch up!");
        else if (dueSoon > 0)
            ColorText.success("🌟 You're on top of it!");
        else
            ColorText.success("✅ All caught up!");
    }

    /** Helper to escape quotes for JSON */
    private static String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}