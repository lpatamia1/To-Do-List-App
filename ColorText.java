public class ColorText {
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String CYAN = "\u001B[36m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String RED = "\u001B[31m";
    public static final String PURPLE = "\u001B[35m";
    public static final String PINK = "\u001B[95m";
    public static final String GRAY = "\u001B[90m";
    // Add more colors as needed
    // Utility methods for colored messages


    public static void success(String msg) { System.out.println(GREEN + msg + RESET); }
    public static void warn(String msg) { System.out.println(YELLOW + msg + RESET); }
    public static void info(String msg) { System.out.println(CYAN + msg + RESET); }

    public static void banner(String title) {
        System.out.println(PURPLE + "╔══════════════════════════════════════╗" + RESET);
        System.out.println(PURPLE + "  " + title + RESET);
        System.out.println(PURPLE + "╚══════════════════════════════════════╝" + RESET);
    }

    public static void line() {
        System.out.println(PURPLE + "────────────────────────────────────────────" + RESET);
    }
}
