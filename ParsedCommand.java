import java.time.LocalDate;

public class ParsedCommand {
    public Intent intent = Intent.UNKNOWN;
    public String description;
    public String priority;
    public LocalDate due;
    public Integer index;

    public static ParsedCommand unknown() {
        return new ParsedCommand();
    }
}
