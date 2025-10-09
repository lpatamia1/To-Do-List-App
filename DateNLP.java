import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateNLP {
    private static final DateTimeFormatter ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDate parseDate(String raw) {
        if (raw == null) return null;
        String s = raw.trim().toLowerCase();

        try { return LocalDate.parse(s, ISO); } catch (DateTimeParseException ignored) {}

        LocalDate today = LocalDate.now();

        switch (s) {
            case "today": return today;
            case "tomorrow": return today.plusDays(1);
            case "next week": return today.plusWeeks(1);
            case "next month": return today.plusMonths(1);
        }

        if (s.matches("in \\d+ days?")) {
            int n = Integer.parseInt(s.replaceAll("\\D+", ""));
            return today.plusDays(n);
        }

        if (s.matches("\\d{1,2}/\\d{1,2}")) {
            String[] parts = s.split("/");
            int m = Integer.parseInt(parts[0]);
            int d = Integer.parseInt(parts[1]);
            LocalDate candidate = LocalDate.of(today.getYear(), m, d);
            if (candidate.isBefore(today)) candidate = candidate.plusYears(1);
            return candidate;
        }

        return null;
    }
}
