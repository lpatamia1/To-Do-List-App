public class ColorText {
    // Reset color back to normal
    public static final String RESET = "\u001B[0m";

    // Standard colors
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    // Optional: pastel / retro-like background shades
    public static final String BG_PINK = "\u001B[45m";
    public static final String BG_TEAL = "\u001B[46m";
    public static final String BG_GRAY = "\u001B[47m";

    public static void main(String[] args) {
        System.out.println(CYAN + "✨ Welcome to the Retro Console! ✨" + RESET);
        System.out.println(GREEN + "✅ Task completed successfully!" + RESET);
        System.out.println(YELLOW + "⚡ Keep going, you’re doing great!" + RESET);
        System.out.println(PURPLE + "🌈 Have a colorful day!" + RESET);
    }
}
