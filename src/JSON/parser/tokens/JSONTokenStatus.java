package JSON.parser.tokens;

import java.util.Arrays;
import java.util.EnumSet;

public enum JSONTokenStatus {
    // Error Code
    GOOD_TOKEN("Good token"),
//    INVALID_CHARACTER("Invalid character"),
    UNEXPECTED_TOKEN("Unexpected token"),
    UNEXPECTED_EOF("Unexpected end of file"),
    RANGE_INTEGER("Integer literal out of range"),
    RANGE_REAL("Real literal out of range");

    private static final int FIRST_ERROR_INDEX = UNEXPECTED_TOKEN.ordinal();
    private static final int LAST_ERROR_INDEX  = RANGE_REAL.ordinal();

    private final String text;  // token text

    JSONTokenStatus() {
        text = this.toString().toLowerCase();
    }

    JSONTokenStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static EnumSet<JSONTokenStatus> ERROR_STATUS_SET =
            EnumSet.noneOf(JSONTokenStatus.class);

    static {
        JSONTokenStatus[] values = JSONTokenStatus.values();
        ERROR_STATUS_SET.addAll(Arrays.asList(values)
                .subList(FIRST_ERROR_INDEX, LAST_ERROR_INDEX + 1));
    }
}
