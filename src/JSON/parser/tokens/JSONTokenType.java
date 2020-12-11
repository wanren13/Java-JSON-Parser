package JSON.parser.tokens;

import java.util.EnumSet;
import java.util.Hashtable;

public enum JSONTokenType {
    // Special symbols.
    //additive operators
    PLUS("+"), MINUS("-"), DOT("."),

    //other
    SLASH("/"), QUOTE("\""),

    COMMA(","), COLON(":"),
    //parens
    LEFT_BRACKET("["), RIGHT_BRACKET("]"),
    LEFT_BRACE("{"), RIGHT_BRACE("}"),

    // value types
    INTEGER, REAL, STRING, BOOLEAN, NULL,

    // undefined token
    UNDEFINED,

    //special tokens
    END_OF_FILE("End of file");


    private static final int FIRST_SPECIAL_INDEX = COMMA.ordinal();
    private static final int LAST_SPECIAL_INDEX  = RIGHT_BRACE.ordinal();

    private final String text;  // token text

    JSONTokenType() {
        this.text = this.toString().toLowerCase();
    }

    JSONTokenType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static final EnumSet<JSONTokenType> SINGLE_VALUE_SET =
            EnumSet.of(INTEGER, REAL, STRING, BOOLEAN, NULL);
    public static Hashtable<String, JSONTokenType> SPECIAL_SYMBOLS = new Hashtable<>();

    static {
        JSONTokenType[] values = JSONTokenType.values();
        for (int i = FIRST_SPECIAL_INDEX; i <= LAST_SPECIAL_INDEX; ++i) {
            SPECIAL_SYMBOLS.put(values[i].getText(), values[i]);
        }
    }
}

