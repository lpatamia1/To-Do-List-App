import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.time.LocalDate;

public class todo {
    private static final List<Task> tasks = new ArrayList<>();
    private static final SimpleDateFormat timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static int addedToday = 0;
    private static int completedToday = 0;

    public static void main(String[] args) {
        retroBoot();
        loadTasksJSON();
        Scanner scanner = new Scanner(System.in);
        ColorText.banner("✨ Retro To-Do List ✨");
        showQuote();

        while (true) {
            ColorText.line();
            System.out.println("1️⃣  Add Task\n2️⃣  View Tasks\n3️⃣  Mark Complete\n4️⃣  Show Upcoming\n5️⃣  Export Markdown\n6️⃣  Exit");
            System.out.print(ColorText.CYAN + "Choose: " + ColorText.RESET);
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": addTask(scanner); break;
                case "2": showTasks(); break;
                case "3": completeTask(scanner); break;
                case "4": showUpcoming(); break;
                case "5": exportMarkdown(); break;
                case "6": exitApp(); scanner.close(); return;
                default: beep(); ColorText.warn("❓ Invalid option. Try again!");
            }
        }
    }

    // --- Core Features ---

    private static void addTask(Scanner scanner) {
        System.out.print(ColorText.YELLOW + "Enter task: " + ColorText.RESET);
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            beep();
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
        System.out.printf(ColorText.CYAN + "%-3s %-37s %-19s %-12s%n" + ColorText.RESET,
                "#", "Task", "Priority", "Due Date");
        System.out.println(ColorText.CYAN + "───────────────────────────────────────────────────────────────────────" + ColorText.RESET);

        // --- Table Rows ---
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
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
            System.out.printf(ColorText.BLUE + "%-3d %-37s %-27s %-12s" + ColorText.RESET + "%n",
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

    private static void showUpcoming() {
        ColorText.info("\n📅 Tasks due within 3 days:");
        boolean found = false;
        for (Task t : tasks) {
            LocalDate due = t.getDue();
            if (due != null && !LocalDate.now().isAfter(due) && !due.isAfter(LocalDate.now().plusDays(3))) {
                System.out.println(ColorText.YELLOW + "⏰ " + t.getName() + " → " + due + ColorText.RESET);
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

        int totalSteps = 30;
        for (int i = 0; i <= totalSteps; i++) {
            int progress = (i * 100) / totalSteps;
            String bar = "█".repeat(i) + "▒".repeat(totalSteps - i);
            System.out.print("\r" + ColorText.PINK + "Loading: [" + bar + "] " + progress + "%" + ColorText.RESET);
            try { Thread.sleep(90); } catch (InterruptedException ignored) {}
        }

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
            "Retro vibes only ✨",
            "Every bug teaches you something 🐛",
            "One task at a time 🪩",
            "Stay groovy and productive! 🎸",
            "You are doing great! 🌟"
        };
        Random rand = new Random();
        System.out.println(ColorText.CYAN + "💬 " + quotes[rand.nextInt(quotes.length)] + ColorText.RESET);
    }

    private static void exitApp() {
        beep();
        ColorText.line();
        ColorText.success("Exiting… your tasks are saved!");
        ColorText.info("📅 Today’s Stats: Added " + addedToday + " | Completed " + completedToday);
        saveTasksJSON();
        exportMarkdown();
    }

    private static void beep() {
        System.out.print("\007");
        System.out.flush();
    }

    // --- Utility Helpers ---

    private static void pauseAndClear(Scanner scanner) {
        System.out.println("\nPress Enter to return to menu...");
        scanner.nextLine();
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
        try (PrintWriter out = new PrintWriter(new FileWriter(JSON_FILE))) {
            out.println("[");
            for (int i = 0; i < tasks.size(); i++) {
                Task t = tasks.get(i);
                out.print("  {");
                out.printf("\"name\":\"%s\", ", escapeJson(t.getName()));
                out.printf("\"priority\":\"%s\", ", t.getPriority());
                out.printf("\"completed\":%s, ", t.isCompleted());
                out.printf("\"due\":%s", (t.getDue() == null ? "null" : "\"" + t.getDue() + "\""));
                out.print("}");
                if (i < tasks.size() - 1) out.println(",");
                else out.println();
            }
            out.println("]");
            ColorText.info("Tasks saved to tasks.json");
        } catch (IOException e) {
            ColorText.warn("⚠️ Could not save tasks.json: " + e.getMessage());
        }
    }

    /** Load tasks back from the JSON file if it exists */
    private static void loadTasksJSON() {
        File file = new File(JSON_FILE);
        if (!file.exists()) return;

        try (Scanner reader = new Scanner(file)) {
            StringBuilder json = new StringBuilder();
            while (reader.hasNextLine()) json.append(reader.nextLine().trim());
            String content = json.toString();

            // Parse manually (since we're not using a JSON library)
            String[] objects = content.split("\\},\\s*\\{");
            for (String obj : objects) {
                String cleaned = obj.replaceAll("[\\[\\]{}\"]", "").trim();
                if (cleaned.isEmpty()) continue;

                Map<String, String> map = new HashMap<>();
                for (String part : cleaned.split(",")) {
                    String[] kv = part.split(":", 2);
                    if (kv.length == 2) map.put(kv[0].trim(), kv[1].trim());
                }

                String name = map.getOrDefault("name", "Untitled");
                String priority = map.getOrDefault("priority", "NONE");
                LocalDate due = null;
                String dueRaw = map.getOrDefault("due", "").replace("\"", "").trim();
                if (!dueRaw.isEmpty() && !"null".equalsIgnoreCase(dueRaw) && !"none".equalsIgnoreCase(dueRaw))
                    due = LocalDate.parse(dueRaw);
                else
                    due = null;
                Task t = new Task(name, priority, due);
                if ("true".equalsIgnoreCase(map.getOrDefault("completed", "false")))
                    t.markCompleted();

                tasks.add(t);
            }
            Collections.sort(tasks);
            ColorText.success("📂 Loaded " + tasks.size() + " task(s) from tasks.json");
        } catch (Exception e) {
            ColorText.warn("⚠️ Could not load tasks.json: " + e.getMessage());
        }
    }

    /** Helper to escape quotes for JSON */
    private static String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}