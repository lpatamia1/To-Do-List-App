import java.time.LocalDate;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleNLP {
    private static final Pattern QUOTED = Pattern.compile("\"([^\"]+)\"");
    private static final Pattern DUE = Pattern.compile("\\b(due|by|on|before)\\s+([\\w\\-/]+(?:\\s\\w+)*)", Pattern.CASE_INSENSITIVE);
    private static final Pattern PRIO = Pattern.compile("\\b(high|medium|low|h|m|l)\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern COMPLETE = Pattern.compile("\\b(complete|done|finish)\\s+(\\d+)\\b");

    public static ParsedCommand parse(String raw) {
        if (raw == null || raw.trim().isEmpty()) return ParsedCommand.unknown();

        String s = raw.trim();
        String low = s.toLowerCase(Locale.ROOT);

        ParsedCommand pc = new ParsedCommand();

        if (low.matches("^(view|show|list) tasks?$")) { pc.intent = Intent.VIEW; return pc; }
        if (low.contains("upcoming")) { pc.intent = Intent.SHOW_UPCOMING; return pc; }
        if (low.startsWith("export")) { pc.intent = Intent.EXPORT; return pc; }
        if (low.startsWith("help")) { pc.intent = Intent.HELP; return pc; }
        if (low.matches("^(exit|quit)$")) { pc.intent = Intent.EXIT; return pc; }

        Matcher cm = COMPLETE.matcher(low);
        if (cm.find()) {
            pc.intent = Intent.COMPLETE;
            pc.index = Integer.parseInt(cm.group(2));
            return pc;
        }

        if (low.startsWith("add") || low.startsWith("new") || low.startsWith("create")) {
            pc.intent = Intent.ADD;

            Matcher q = QUOTED.matcher(s);
            if (q.find()) pc.description = q.group(1);
            else pc.description = s.replaceFirst("(?i)^(add|new|create)\\s+", "");

            Matcher pm = PRIO.matcher(low);
            if (pm.find()) {
                String p = pm.group(1).toUpperCase(Locale.ROOT);
                if (p.startsWith("H")) pc.priority = "H";
                else if (p.startsWith("M")) pc.priority = "M";
                else if (p.startsWith("L")) pc.priority = "L";
            }

            Matcher dm = DUE.matcher(low);
            if (dm.find()) pc.due = DateNLP.parseDate(dm.group(2));
            else {
                if (low.endsWith(" today")) pc.due = LocalDate.now();
                if (low.endsWith(" tomorrow")) pc.due = LocalDate.now().plusDays(1);
            }
            return pc;
        }

        return ParsedCommand.unknown();
    }
}
